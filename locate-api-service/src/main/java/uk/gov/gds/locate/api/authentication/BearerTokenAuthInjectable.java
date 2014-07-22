package uk.gov.gds.locate.api.authentication;

import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gds.locate.api.configuration.LocateApiConfiguration;
import uk.gov.gds.locate.api.dao.UsageDao;
import uk.gov.gds.locate.api.model.AuthorizationToken;
import uk.gov.gds.locate.api.model.Usage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class BearerTokenAuthInjectable extends AbstractHttpContextInjectable {
    private static final Logger LOGGER = LoggerFactory.getLogger(BearerTokenAuthInjectable.class);

    private static final String PREFIX = "Bearer";
    private static final String CHALLENGE_FORMAT = PREFIX + " token";

    private final LocateApiConfiguration configuration;
    private final Authenticator<BearerToken, AuthorizationToken> authenticator;
    private final UsageDao usageDao;


    public BearerTokenAuthInjectable(LocateApiConfiguration configuration, Authenticator<BearerToken, AuthorizationToken> authenticator, UsageDao usageDao) {
        this.configuration = configuration;
        this.authenticator = authenticator;
        this.usageDao = usageDao;
    }

    private WebApplicationException constructWebApplicationException(String message) {
        return new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, CHALLENGE_FORMAT)
                .entity(String.format("{\"error\":\"%s\"}", message))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build());
    }

    private WebApplicationException constructRateLimitedException(Integer used) {
        return new WebApplicationException(Response.status(429)
                .header(HttpHeaders.WWW_AUTHENTICATE, CHALLENGE_FORMAT)
                .header("X-Locate-Limit-Max", configuration.getMaxRequestsPerDay())
                .header("X-Locate-Limit-Used", used)
                .entity(String.format("{\"error\":\"Exceed usage limits\"}"))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build());
    }

    @Override
    public AuthorizationToken getValue(HttpContext context) {
        try {
            Optional<String> bearerToken = getBearerToken(context.getRequest());

            if (bearerToken.isPresent()) {
                return authenticateClientByBearerToken(bearerToken.get(), context);
            } else {
                throw constructWebApplicationException("Invalid credentials");

            }
        } catch (IllegalArgumentException e) {
            LOGGER.debug("Error decoding credentials: Illegal Argument", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException e) {
            LOGGER.warn("Error authenticating credentials", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private Optional<String> getBearerToken(HttpRequestContext request) {
        final String authorizationHeader = request.getHeaderValue(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null) {
            String[] authorizationHeaderWords = authorizationHeader.split("\\s+");

            if (authorizationHeaderWords.length == 2 && PREFIX.equalsIgnoreCase(authorizationHeaderWords[0])) {
                return Optional.of(authorizationHeaderWords[1]);
            }
        }

        return Optional.absent();
    }

    private AuthorizationToken authenticateClientByBearerToken(String token, HttpContext context) throws AuthenticationException {
        final BearerToken bearerToken = new BearerToken(token);
        final Optional<AuthorizationToken> result = authenticator.authenticate(bearerToken);

        if (result.isPresent()) {
            Integer requests = requestsMade(result.get());
            if (requests > configuration.getMaxRequestsPerDay()) {
                throw constructRateLimitedException(requests);
            }
            // update count
            return result.get();
        }
        throw constructWebApplicationException("Invalid credentials");
    }

    private Integer requestsMade(AuthorizationToken token) {
        Optional<Usage> rateMeter = usageDao.findRateMeterById(token.getIdentifier());
        if (rateMeter.isPresent()) return rateMeter.get().getCount();
        else return 0;  // create a rateMeter document
    }
}
