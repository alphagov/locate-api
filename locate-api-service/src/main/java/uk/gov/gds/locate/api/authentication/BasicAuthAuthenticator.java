package uk.gov.gds.locate.api.authentication;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;
import uk.gov.gds.locate.api.dao.AuthorizationTokenDao;
import uk.gov.gds.locate.api.model.AuthorizationToken;

public class BasicAuthAuthenticator implements Authenticator<BasicCredentials, String> {

    public Optional<String> authenticate(BasicCredentials creds) throws AuthenticationException {
        return Optional.of("martyn");
    }
}
