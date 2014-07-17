package uk.gov.gds.locate.api.authentication;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import uk.gov.gds.locate.api.dao.AuthorizationTokenDao;
import uk.gov.gds.locate.api.model.AuthorizationToken;

public class BearerTokenAuthenticator implements Authenticator<BearerToken, AuthorizationToken> {

    private final AuthorizationTokenDao authorizationTokenDao;

    public BearerTokenAuthenticator(AuthorizationTokenDao authorizationTokenDao) {
        this.authorizationTokenDao = authorizationTokenDao;
    }

    public Optional<AuthorizationToken> authenticate(BearerToken bearerToken) throws AuthenticationException {
        return Optional.fromNullable(authorizationTokenDao.fetchCredentialsByBearerToken(bearerToken.getBearerToken()));
    }
}
