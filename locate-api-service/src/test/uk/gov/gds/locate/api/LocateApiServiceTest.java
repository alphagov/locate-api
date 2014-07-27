package uk.gov.gds.locate.api;

import com.mongodb.MongoException;
import com.sun.jersey.api.core.ResourceConfig;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.json.ObjectMapperFactory;
import org.junit.Before;
import org.junit.Test;
import uk.gov.gds.locate.api.authentication.BearerTokenAuthProvider;
import uk.gov.gds.locate.api.configuration.LocateApiConfiguration;
import uk.gov.gds.locate.api.configuration.MongoConfiguration;
import uk.gov.gds.locate.api.healthchecks.MongoHealthCheck;
import uk.gov.gds.locate.api.resources.AddressResource;
import uk.gov.gds.locate.api.resources.PostcodeToAuthorityResource;

import java.util.Collections;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

public class LocateApiServiceTest {

    private LocateApiService locateApiService = new LocateApiService();
    private Environment environment = mock(Environment.class);
    private LocateApiConfiguration configuration = mock(LocateApiConfiguration.class);
    private MongoConfiguration mongoConfiguration = mock(MongoConfiguration.class);
    private ResourceConfig resourceConfig = mock(ResourceConfig.class);
    private ObjectMapperFactory objectMapperFactory = mock(ObjectMapperFactory.class);

    @Before
    public void setUp() {
        when(mongoConfiguration.getLocateDatabase()).thenReturn("locate");
        when(mongoConfiguration.getCredentialsDatabase()).thenReturn("locate");
        when(mongoConfiguration.getHosts()).thenReturn("localhost");
        when(mongoConfiguration.getPort()).thenReturn(27017);

        when(configuration.getMongoConfiguration()).thenReturn(mongoConfiguration);

        when(environment.getJerseyResourceConfig()).thenReturn(resourceConfig);
        when(environment.getObjectMapperFactory()).thenReturn(objectMapperFactory);
        when(resourceConfig.getSingletons()).thenReturn(Collections.EMPTY_SET);
    }

    @Test
    public void shouldAddAddressResourceToEnvironment() throws Exception {
        locateApiService.run(configuration, environment);
        verify(environment, times(1)).addResource(isA(AddressResource.class));
        verify(environment, times(1)).addResource(isA(PostcodeToAuthorityResource.class));
    }

    @Test
    public void shouldAddMongoHealthcheckToEnvironment() throws Exception {
        locateApiService.run(configuration, environment);
        verify(environment, times(1)).addHealthCheck(isA(MongoHealthCheck.class));
    }

    @Test
    public void shouldAddCustomExceptionMapperToEnvironment() throws Exception {
        locateApiService.run(configuration, environment);
        verify(environment, times(1)).addProvider(isA(LocateExceptionMapper.class));
    }

    @Test
    public void shouldAddAuthenticationProviders() throws Exception {
        locateApiService.run(configuration, environment);
        verify(environment, times(1)).addProvider(isA(BearerTokenAuthProvider.class));
    }

    @Test
    public void shouldSetUpMongoAuthIfRequired() throws Exception {
        when(mongoConfiguration.getUsername()).thenReturn("username");
        when(mongoConfiguration.getPassword()).thenReturn("password");
        when(mongoConfiguration.requiresAuth()).thenReturn(true);
        try {
            locateApiService.run(configuration, environment);
        } catch (MongoException e) {
            // expected as no mongo db  live
            verify(mongoConfiguration, times(1)).getUsername();
            verify(mongoConfiguration, times(1)).getPassword();
        }
    }

    @Test
    public void shouldNotSetUpMongoAuthIfNotRequired() throws Exception {
        when(mongoConfiguration.requiresAuth()).thenReturn(false);
        locateApiService.run(configuration, environment);
        verify(mongoConfiguration, times(0)).getUsername();
        verify(mongoConfiguration, times(0)).getPassword();
    }

    @Test
    public void shouldSetUpBothCredentialsAndLocateDatabases() throws Exception {
        when(mongoConfiguration.requiresAuth()).thenReturn(false);
        locateApiService.run(configuration, environment);
        verify(mongoConfiguration, times(1)).getLocateDatabase();
        verify(mongoConfiguration, times(1)).getCredentialsDatabase();
    }

}