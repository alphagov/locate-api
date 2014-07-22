package uk.gov.gds.locate.api.authentication;

import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.auth.Authenticator;
import uk.gov.gds.locate.api.configuration.LocateApiConfiguration;
import uk.gov.gds.locate.api.dao.UsageDao;
import uk.gov.gds.locate.api.model.AuthorizationToken;

public class BearerTokenAuthProvider implements InjectableProvider<Auth, Parameter> {

    private final LocateApiConfiguration configuration;
    private final UsageDao usageDao;
    private final Authenticator<BearerToken, AuthorizationToken> authenticator;


    public BearerTokenAuthProvider(LocateApiConfiguration configuration, UsageDao usageDao, Authenticator<BearerToken, AuthorizationToken> bearerTokenAuthenticator) {
        this.configuration = configuration;
        this.usageDao = usageDao;
        this.authenticator = bearerTokenAuthenticator;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override
    public Injectable<?> getInjectable(ComponentContext ic, Auth a, Parameter c) {
        return new BearerTokenAuthInjectable(configuration, authenticator, usageDao);
    }
}
