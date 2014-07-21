package uk.gov.gds.locate.api.resources;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.testing.ResourceTest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import uk.gov.gds.locate.api.LocateExceptionMapper;
import uk.gov.gds.locate.api.authentication.BearerToken;
import uk.gov.gds.locate.api.authentication.BearerTokenAuthProvider;
import uk.gov.gds.locate.api.configuration.LocateApiConfiguration;
import uk.gov.gds.locate.api.dao.AddressDao;
import uk.gov.gds.locate.api.model.*;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AddressResourceTest extends ResourceTest {

    private LocateApiConfiguration configuration = mock(LocateApiConfiguration.class);
    private String validToken = String.format("Bearer %s", "valid");
    private String inValidToken = String.format("Bearer %s", "bogus");

    private String validPostcode = "a11aa";
    private String inValidPostcode = "bogus";
    private AuthorizationToken authorizationToken = new AuthorizationToken("1", "identifier", "token", 1);

    private Address address = new Address("gssCode", "postcode", new Presentation(), new Details());

    private AddressDao dao = mock(AddressDao.class);

    @Before
    public void setUp() {
        when(dao.findAllForPostcode(validPostcode)).thenReturn(ImmutableList.of(address));
        when(dao.findAllForPostcode(inValidPostcode)).thenReturn(Collections.<Address>emptyList());
        when(configuration.getMaxRequestsPerDay()).thenReturn(1);
    }

    @Test
    public void shouldRejectAddressesFetchWithNoAuthCredentials() {
        try {
            client().resource("/locate/addresses").get(Object.class);
            fail("Should have rejected an API call with no auth headers");
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus(), Matchers.is(401));
            assertThat(e.getResponse().getEntity(String.class), Matchers.is("{\"error\":\"Invalid credentials\"}"));
        }
    }

    @Test
    public void shouldRejectAddressesFetchWithInvalidAuthCredentials() {
        try {
            client().resource("/locate/addresses").header("Authorization", inValidToken).get(Object.class);
            fail("Should have rejected an API call with no auth headers");
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus(), Matchers.is(401));
            assertThat(e.getResponse().getEntity(String.class), Matchers.is("{\"error\":\"Invalid credentials\"}"));
        }
    }

    @Test
    public void shouldAllowAddressesFetchWithValidAuthCredentials() {
        try {
            client().resource("/locate/addresses").header("Authorization", validToken).get(Object.class);
        } catch (UniformInterfaceException e) {
            fail("Should have rejected an API call with no auth headers");
        }
    }

    @Test
    public void shouldReturnAListOfAddressesForASuccessfulSearch() {
        List<SimpleAddress> result = client().resource("/locate/addresses?postcode=" + validPostcode).header("Authorization", validToken).get(new GenericType<List<SimpleAddress>>() {
        });
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getGssCode()).isEqualTo(address.getGssCode());
        assertThat(result.get(0).getUprn()).isEqualTo(address.getUprn());
        verify(dao, times(1)).findAllForPostcode(validPostcode);
    }

    @Test
    public void shouldReturnAListOfAddressesForAnUnsuccessfulSearch() {
        List<SimpleAddress> result = client().resource("/locate/addresses?postcode=" + inValidPostcode).header("Authorization", validToken).get(new GenericType<List<SimpleAddress>>() {
        });
        assertThat(result.size()).isEqualTo(0);
        verify(dao, times(1)).findAllForPostcode(inValidPostcode);
    }

    @Override
    protected void setUpResources() throws Exception {
        addResource(new AddressResource(dao));
        addProvider(new BearerTokenAuthProvider(configuration, new TestAuthenticator()));
        addProvider(new LocateExceptionMapper());
    }

    protected class TestAuthenticator implements Authenticator<BearerToken, AuthorizationToken> {

        public Optional<AuthorizationToken> authenticate(BearerToken bearerToken) throws AuthenticationException {

            if ("valid".equalsIgnoreCase(bearerToken.getBearerToken())) {
                return Optional.of(authorizationToken);
            } else {
                return Optional.absent();
            }
        }
    }
}