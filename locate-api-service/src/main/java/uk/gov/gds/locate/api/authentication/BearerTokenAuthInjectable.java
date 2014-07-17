package uk.gov.gds.locate.api.authentication;

import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gds.locate.api.model.AuthorizationToken;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class BearerTokenAuthInjectable extends AbstractHttpContextInjectable {
    private static final Logger LOGGER = LoggerFactory.getLogger(BearerTokenAuthInjectable.class);

    private static final String PREFIX = "Bearer";
    private static final String CHALLENGE_FORMAT = PREFIX + " token";

    private final Authenticator<BearerToken, AuthorizationToken> authenticator;

    public BearerTokenAuthInjectable(Authenticator<BearerToken, AuthorizationToken> authenticator) {
        this.authenticator = authenticator;
    }

    private WebApplicationException constructWebApplicationException(String message) {
        return new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, CHALLENGE_FORMAT)
                .entity(String.format("{\"error\":\"%s\"}", message))
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
            return result.get();
        }
        throw constructWebApplicationException("Invalid credentials");
    }
}
