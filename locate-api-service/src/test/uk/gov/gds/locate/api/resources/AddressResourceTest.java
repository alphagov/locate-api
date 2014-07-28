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
import uk.gov.gds.locate.api.authentication.BearerTokenAuthProvider;
import uk.gov.gds.locate.api.configuration.LocateApiConfiguration;
import uk.gov.gds.locate.api.dao.AddressDao;
import uk.gov.gds.locate.api.dao.UsageDao;
import uk.gov.gds.locate.api.helpers.DetailsBuilder;
import uk.gov.gds.locate.api.helpers.OrderingBuilder;
import uk.gov.gds.locate.api.helpers.PresentationBuilder;
import uk.gov.gds.locate.api.model.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AddressResourceTest extends ResourceTest {

    private AddressDao dao = mock(AddressDao.class);
    private UsageDao usageDao = mock(UsageDao.class);
    private LocateApiConfiguration configuration = mock(LocateApiConfiguration.class);

    private String allDataFieldsToken = String.format("Bearer %s", "all-fields");
    private String presentationDataFieldsToken = String.format("Bearer %s", "presentation-fields");
    private String inValidToken = String.format("Bearer %s", "bogus");
    private String validPostcode = "a11aa";
    private String inValidPostcode = "bogus";

    private AuthorizationToken allFieldsAuthorizationToken = new AuthorizationToken("1", "name", "identifier", "organisation", "token", QueryType.ALL, DataType.ALL);
    private AuthorizationToken presentationFieldsAuthorizationToken = new AuthorizationToken("1", "name", "identifier", "organisation", "token", QueryType.ALL, DataType.PRESENTATION);
    private Details validAddress = new DetailsBuilder("test").postal(true).residential(true).electoral(true).build();
    private Ordering ordering = new OrderingBuilder().build();
    private Address address = new Address("gssCode", "uprn", "postcode", "country", new Date(), new PresentationBuilder("test").build(), validAddress, new Location(), ordering, "iv");
    private Usage usage = new Usage("id", "identifier", 1, new Date());

    @Before
    public void setUp() {
        when(usageDao.findUsageByIdentifier("identifier")).thenReturn(Optional.of(usage));
        when(dao.findAllForPostcode(validPostcode)).thenReturn(ImmutableList.of(address));
        when(dao.findAllForPostcode(inValidPostcode)).thenReturn(Collections.<Address>emptyList());
        when(configuration.getMaxRequestsPerDay()).thenReturn(1);
        when(configuration.getEncryptionKey()).thenReturn("key");
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
    public void shouldRejectAddressesFetchWithExceededUsage() {
        Usage exceededUsage = new Usage("id", "identifier", 100, new Date());
        when(configuration.getMaxRequestsPerDay()).thenReturn(1);
        when(usageDao.findUsageByIdentifier("identifier")).thenReturn(Optional.of(exceededUsage));

        try {
            client().resource("/locate/addresses").header("Authorization", allDataFieldsToken).get(Object.class);
            fail("Should have rejected an API call with no exceeded usage");
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus(), Matchers.is(429));
            assertThat(e.getResponse().getEntity(String.class), Matchers.is("{\"error\":\"Exceed usage limits\"}"));
        }
    }

    @Test
    public void shouldAllowAddressesFetchWithValidAuthCredentials() {
        try {
            client().resource("/locate/addresses").header("Authorization", allDataFieldsToken).get(Object.class);
        } catch (UniformInterfaceException e) {
            fail("Should not have rejected an API call with valid auth headers");
        }
    }

    @Test
    public void shouldHaveAValidationFailureIfPostcodeTooLong() {
        try {
            client().resource("/locate/addresses?postcode=12345678901").header("Authorization", allDataFieldsToken).get(Object.class);
            fail("Fail should have been a validation error");
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(422);
            assertThat(e.getResponse().getEntity(String.class)).isEqualTo("{\"error\":\"postcode is invalid\"}");
        }
    }

    @Test
    public void shouldReturnAListOfAddressesForASuccessfulSearchWithAllFieldsToken() {
        List<Address> result = client().resource("/locate/addresses?postcode=" + validPostcode).header("Authorization", allDataFieldsToken).get(new GenericType<List<Address>>() {
        });
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getGssCode()).isEqualTo(address.getGssCode());
        assertThat(result.get(0).getUprn()).isEqualTo(address.getUprn());
        assertThat(result.get(0).getDetails()).isNotNull();
        assertThat(result.get(0).getPresentation()).isNotNull();
        assertThat(result.get(0).getLocation()).isNotNull();
        verify(dao, times(1)).findAllForPostcode(validPostcode);
    }

    @Test
    public void shouldReturnAListOfAddressesAsValidJSONForASuccessfulSearchWithAllFieldsToken() {
        String result = client().resource("/locate/addresses?postcode=" + validPostcode).header("Authorization", allDataFieldsToken).get(String.class);
        verify(dao, times(1)).findAllForPostcode(validPostcode);
        assertThat(result).contains("\"gssCode\":\"gssCode\"");
        assertThat(result).contains("\"uprn\":\"uprn\"");
        assertThat(result).contains("\"presentation\"");
        assertThat(result).contains("\"details\"");
        assertThat(result).contains("\"location\"");
        assertThat(result).contains("\"property\":\"property-test\"");
        assertThat(result).contains("\"street\":\"street-test\"");
        assertThat(result).contains("\"locality\":\"locality-test\"");
        assertThat(result).contains("\"town\":\"town-test\"");
        assertThat(result).contains("\"area\":\"area-test\"");
        assertThat(result).contains("\"postcode\":\"postcode-test\"");
    }

    @Test
    public void shouldReturnAListOfAddressesForASuccessfulSearchWithPresentationFieldsToken() {
        List<SimpleAddress> result = client().resource("/locate/addresses?postcode=" + validPostcode).header("Authorization", presentationDataFieldsToken).get(new GenericType<List<SimpleAddress>>() {
        });
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getGssCode()).isEqualTo(address.getGssCode());
        assertThat(result.get(0).getUprn()).isEqualTo(address.getUprn());
        assertThat(result.get(0).getProperty()).isEqualTo("property-test");
        assertThat(result.get(0).getStreet()).isEqualTo("street-test");
        assertThat(result.get(0).getLocality()).isEqualTo("locality-test");
        assertThat(result.get(0).getArea()).isEqualTo("area-test");
        assertThat(result.get(0).getTown()).isEqualTo("town-test");
        assertThat(result.get(0).getPostcode()).isEqualTo("postcode-test");
        verify(dao, times(1)).findAllForPostcode(validPostcode);
    }

    @Test
    public void shouldReturnAListOfAddressesAsValidJSONForASuccessfulSearchWithPresentationFieldsToken() {
        String result = client().resource("/locate/addresses?postcode=" + validPostcode).header("Authorization", presentationDataFieldsToken).get(String.class);
        verify(dao, times(1)).findAllForPostcode(validPostcode);
        assertThat(result).contains("\"gssCode\":\"gssCode\"");
        assertThat(result).contains("\"uprn\":\"uprn\"");
        assertThat(result).doesNotContain("\"presentation\"");
        assertThat(result).doesNotContain("\"details\"");
        assertThat(result).doesNotContain("\"location\"");
        assertThat(result).contains("\"property\":\"property-test\"");
        assertThat(result).contains("\"street\":\"street-test\"");
        assertThat(result).contains("\"locality\":\"locality-test\"");
        assertThat(result).contains("\"town\":\"town-test\"");
        assertThat(result).contains("\"area\":\"area-test\"");
        assertThat(result).contains("\"postcode\":\"postcode-test\"");
    }


    @Test
    public void shouldReturnAListOfAddressesWithoutNullFieldsAsValidJSONForASuccessfulSearch() {
        Presentation presentation = new Presentation();

        Address addressWithMissingFields = new Address("gssCode", "uprn", null, "country", new Date(), presentation, validAddress, new Location(), new Ordering(), "iv");
        when(dao.findAllForPostcode(validPostcode)).thenReturn(ImmutableList.of(addressWithMissingFields));

        String result = client().resource("/locate/addresses?postcode=" + validPostcode).header("Authorization", allDataFieldsToken).get(String.class);
        verify(dao, times(1)).findAllForPostcode(validPostcode);
        assertThat(result).contains("\"gssCode\":\"gssCode\"");
        assertThat(result).contains("\"uprn\":\"uprn\"");
        assertThat(result).doesNotContain("\"property\"");
        assertThat(result).doesNotContain("\"street\"");
        assertThat(result).doesNotContain("\"locality\"");
        assertThat(result).doesNotContain("\"town\"");
        assertThat(result).doesNotContain("\"area\"");
        assertThat(result).doesNotContain("\"postcode\"");
    }


    @Test
    public void shouldReturnAnEmptyListOfAddressesForAnUnsuccessfulSearch() {
        List<SimpleAddress> result = client().resource("/locate/addresses?postcode=" + inValidPostcode).header("Authorization", allDataFieldsToken).get(new GenericType<List<SimpleAddress>>() {
        });
        assertThat(result.size()).isEqualTo(0);
        verify(dao, times(1)).findAllForPostcode(inValidPostcode);
    }

    @Test
    public void shouldReturnAnEmptyListOfAddressesAsValidJSONForAnUnsuccessfulSearch() {
        String result = client().resource("/locate/addresses?postcode=" + inValidPostcode).header("Authorization", allDataFieldsToken).get(String.class);
        assertThat(result).contains("[]");
        verify(dao, times(1)).findAllForPostcode(inValidPostcode);
    }

    @Override
    protected void setUpResources() throws Exception {
        addResource(new AddressResource(dao, configuration));
        addProvider(new BearerTokenAuthProvider(configuration, usageDao, new TestAuthenticator()));
        addProvider(new LocateExceptionMapper());
    }

    protected class TestAuthenticator implements Authenticator<String, AuthorizationToken> {

        public Optional<AuthorizationToken> authenticate(String bearerToken) throws AuthenticationException {

            if ("all-fields".equalsIgnoreCase(bearerToken)) {
                return Optional.of(allFieldsAuthorizationToken);
            } else if ("presentation-fields".equalsIgnoreCase(bearerToken)) {
                return Optional.of(presentationFieldsAuthorizationToken);
            } else {
                return Optional.absent();
            }
        }
    }
}