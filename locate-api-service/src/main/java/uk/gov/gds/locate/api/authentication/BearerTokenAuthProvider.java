package uk.gov.gds.locate.api.authentication;

import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.auth.Authenticator;
import uk.gov.gds.locate.api.model.AuthorizationToken;

public class BearerTokenAuthProvider implements InjectableProvider<Auth, Parameter> {

    private final Authenticator<BearerToken, AuthorizationToken> authenticator;

    public BearerTokenAuthProvider(Authenticator<BearerToken, AuthorizationToken> authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override
    public Injectable<?> getInjectable(ComponentContext ic, Auth a, Parameter c) {
        return new BearerTokenAuthInjectable(authenticator);
    }
}
