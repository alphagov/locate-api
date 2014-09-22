package uk.gov.gds.locate.api.resources;

import com.google.common.base.Optional;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Before;
import org.junit.Test;
import uk.gov.gds.locate.api.LocateExceptionMapper;
import uk.gov.gds.locate.api.authentication.BearerTokenAuthProvider;
import uk.gov.gds.locate.api.configuration.LocateApiConfiguration;
import uk.gov.gds.locate.api.dao.PostcodeToAuthorityDao;
import uk.gov.gds.locate.api.dao.UsageDao;
import uk.gov.gds.locate.api.model.*;

import java.util.Date;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class PostcodeToAuthorityResourceTest extends ResourceTest {
    private AuthorizationToken valid = new AuthorizationToken("1", "name", "identifier", "organisation", "token");
    private String validToken = String.format("Bearer %s", "token");
    private String inValidToken = String.format("Bearer %s", "bogus");

    private Usage usage = new Usage("id", "identifier", 1, new Date());

    private LocateApiConfiguration configuration = mock(LocateApiConfiguration.class);
    private UsageDao usageDao = mock(UsageDao.class);
    private PostcodeToAuthority postcodeToAuthority = new PostcodeToAuthority("id", "gssCode", "country", "postcode", "name", 1.1, 2.2, 3.3,4.4, "nhs-region","nhs","county","ward");
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
            assertThat(e.getResponse().getStatus()).isEqualTo(401);
            assertThat(e.getResponse().getEntity(String.class)).isEqualTo("{\"error\":\"Invalid credentials\"}");
        }
    }

    @Test
    public void shouldRejectRequestFetchWithInvalidAuthCredentials() {
        try {
            client().resource("/locate/authority?postcode=a11aa").header("Authorization", inValidToken).get(Object.class);
            fail("Should have rejected an API call with no auth headers");
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(401);
            assertThat(e.getResponse().getEntity(String.class)).isEqualTo("{\"error\":\"Invalid credentials\"}");
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
            assertThat(e.getResponse().getStatus()).isEqualTo(429);
            assertThat(e.getResponse().getEntity(String.class)).isEqualTo("{\"error\":\"Exceed usage limits\"}");
        }
    }

    @Test
    public void shouldAllowRequestFetchWithValidAuthCredentials() {
        try {
            client().resource("/locate/authority?postcode=a11aa").header("Authorization", validToken).get(Object.class);
            verify(dao, times(1)).findForPostcode("a11aa");
        } catch (Exception e) {
            fail("Should not have rejected an API call with valid auth headers");
        }
    }

    @Test
    public void shouldHaveAValidationFailureIfPostcodeTooLong() {
        try {
            client().resource("/locate/authority?postcode=12345678901").header("Authorization", validToken).get(Object.class);
            fail("Fail should have been a validation error");
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(422);
            assertThat(e.getResponse().getEntity(String.class)).isEqualTo("{\"error\":\"postcode is invalid\"}");
        }
    }

    @Test
    public void shouldReturn404IfNotFound() {
        when(dao.findForPostcode("postcode")).thenReturn(null);
        try {
            client().resource("/locate/authority?postcode=postcode").header("Authorization", validToken).get(Object.class);
            fail("Fail should have been a validation error");
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(404);
            assertThat(e.getResponse().getEntity(String.class)).isEqualTo("{\"error\":\"not found\"}");
        }
    }


    @Test
    public void shouldReturnAPostcodeToAuthorityObject() {
        PostcodeToAuthority result = client().resource("/locate/authority?postcode=a11aa").header("Authorization", validToken).get(PostcodeToAuthority.class);
        verify(dao, times(1)).findForPostcode("a11aa");
        assertThat(result).isLenientEqualsToByIgnoringFields(postcodeToAuthority, "id");
    }

    @Test
    public void shouldReturnAPostcodeToAuthorityAsValidJson() {
        String result = client().resource("/locate/authority?postcode=a11aa").header("Authorization", validToken).get(String.class);
        verify(dao, times(1)).findForPostcode("a11aa");
        assertThat(result).contains("\"postcode\":\"postcode\"");
        assertThat(result).contains("\"country\":\"country\"");
        assertThat(result).contains("\"gssCode\":\"gssCode\"");
        assertThat(result).contains("\"nhsRegionalHealthAuthority\":\"nhs-region\"");
        assertThat(result).contains("\"nhsHealthAuthority\":\"nhs\"");
        assertThat(result).contains("\"county\":\"county\"");
        assertThat(result).contains("\"ward\":\"ward\"");
        assertThat(result).contains("\"easting\":1.1");
        assertThat(result).contains("\"northing\":2.2");
        assertThat(result).contains("\"lat\":3.3");
        assertThat(result).contains("\"long\":4.4");
        assertThat(result).doesNotContain("id");
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
