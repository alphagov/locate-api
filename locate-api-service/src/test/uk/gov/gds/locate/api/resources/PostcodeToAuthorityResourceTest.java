package uk.gov.gds.locate.api.resources;

import com.google.common.base.Optional;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.testing.ResourceTest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import uk.gov.gds.locate.api.LocateExceptionMapper;
import uk.gov.gds.locate.api.authentication.BearerTokenAuthProvider;
import uk.gov.gds.locate.api.configuration.LocateApiConfiguration;
import uk.gov.gds.locate.api.dao.PostcodeToAuthorityDao;
import uk.gov.gds.locate.api.dao.UsageDao;
import uk.gov.gds.locate.api.model.*;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class PostcodeToAuthorityResourceTest extends ResourceTest {
    private AuthorizationToken valid = new AuthorizationToken("1", "name", "identifier", "organisation", "token", QueryType.ALL, DataType.ALL);
    private String validToken = String.format("Bearer %s", "token");
    private String inValidToken = String.format("Bearer %s", "bogus");

    private Usage usage = new Usage("id", "identifier", 1, new Date());

    private LocateApiConfiguration configuration = mock(LocateApiConfiguration.class);
    private UsageDao usageDao = mock(UsageDao.class);
    private PostcodeToAuthority postcodeToAuthority = new PostcodeToAuthority("id", "gssCode", "country", "name", "postcode");
    private PostcodeToAuthorityDao dao = mock(PostcodeToAuthorityDao.class);

    @Before
    public void setUp() {
        when(usageDao.findUsageByIdentifier("identifier")).thenReturn(Optional.of(usage));
        when(configuration.getMaxRequestsPerDay()).thenReturn(100);
        when(dao.findForPostcode("a11aa")).thenReturn(postcodeToAuthority);
    }

    @Test
    public void shouldRejectRequestFetchWithNoAuthCredentials() {
        try {
            client().resource("/locate/authority?postcode=a11aa").get(Object.class);
            fail("Should have rejected an API call with no auth headers");
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus(), Matchers.is(401));
            assertThat(e.getResponse().getEntity(String.class), Matchers.is("{\"error\":\"Invalid credentials\"}"));
        }
    }

    @Test
    public void shouldRejectRequestFetchWithInvalidAuthCredentials() {
        try {
            client().resource("/locate/authority?postcode=a11aa").header("Authorization", inValidToken).get(Object.class);
            fail("Should have rejected an API call with no auth headers");
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus(), Matchers.is(401));
            assertThat(e.getResponse().getEntity(String.class), Matchers.is("{\"error\":\"Invalid credentials\"}"));
        }
    }

    @Test
    public void shouldRejectAddressesFetchWithExceededUsage() {
        Usage exceededUsage = new Usage("id", "identifier", 100, new Date());
        when(configuration.getMaxRequestsPerDay()).thenReturn(1);
        when(usageDao.findUsageByIdentifier("identifier")).thenReturn(Optional.of(exceededUsage));

        try {
            client().resource("/locate/authority?postcode=a11aa").header("Authorization", validToken).get(Object.class);
            fail("Should have rejected an API call with no exceeded usage");
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus(), Matchers.is(429));
            assertThat(e.getResponse().getEntity(String.class), Matchers.is("{\"error\":\"Exceed usage limits\"}"));
        }
    }

    @Test
    public void shouldAllowRequestFetchWithValidAuthCredentials() {
        try {
            client().resource("/locate/authority?postcode=a11aa").header("Authorization", validToken).get(Object.class);
            verify(dao, times(1)).findForPostcode("a11aa");
        } catch (UniformInterfaceException e) {
            fail("Should not have rejected an API call with valid auth headers");
        }
    }

    @Override
    protected void setUpResources() throws Exception {
        addResource(new PostcodeToAuthorityResource(dao));
        addProvider(new BearerTokenAuthProvider(configuration, usageDao, new TestAuthenticator()));
        addProvider(new LocateExceptionMapper());
    }

    protected class TestAuthenticator implements Authenticator<String, AuthorizationToken> {

        public Optional<AuthorizationToken> authenticate(String bearerToken) throws AuthenticationException {

            if ("token".equalsIgnoreCase(bearerToken)) {
                return Optional.of(valid);
            } else {
                return Optional.absent();
            }
        }
    }
}
