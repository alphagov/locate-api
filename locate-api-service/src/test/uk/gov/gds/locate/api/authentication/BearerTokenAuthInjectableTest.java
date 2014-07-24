package uk.gov.gds.locate.api.authentication;

import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;
import com.yammer.dropwizard.auth.AuthenticationException;
import org.junit.Before;
import org.junit.Test;
import uk.gov.gds.locate.api.configuration.LocateApiConfiguration;
import uk.gov.gds.locate.api.dao.UsageDao;
import uk.gov.gds.locate.api.model.AuthorizationToken;
import uk.gov.gds.locate.api.model.DataType;
import uk.gov.gds.locate.api.model.QueryType;
import uk.gov.gds.locate.api.model.Usage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import java.util.Date;

import static junit.framework.TestCase.fail;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


public class BearerTokenAuthInjectableTest {

    private BearerTokenAuthenticator mockAuthenticator = mock(BearerTokenAuthenticator.class);
    private UsageDao usageDao = mock(UsageDao.class);
    private HttpContext context = mock(HttpContext.class);
    private HttpRequestContext webContext = mock(HttpRequestContext.class);
    private BearerTokenAuthInjectable auth;
    private LocateApiConfiguration configuration = mock(LocateApiConfiguration.class);

    @Before
    public void setUp() {
        when(context.getRequest()).thenReturn(webContext);
        when(configuration.getMaxRequestsPerDay()).thenReturn(1000);
        auth = new BearerTokenAuthInjectable(configuration, mockAuthenticator, usageDao);
    }

    @Test
    public void shouldThrowUnauthorizedWebApplicationExceptionIfTokenDoesNotExistOnRequest() {
        try {
            when(webContext.getHeaderValue(HttpHeaders.AUTHORIZATION)).thenReturn(null);
            when(webContext.getMethod()).thenReturn("GET");
            when(webContext.getPath()).thenReturn("http://localhost/test/path");
            when(webContext.getHeaderValue("X-Real-IP")).thenReturn("192.0.0.1");

            auth.getValue(context);
            fail("Should have thrown an unauthorized web exception");
        } catch (WebApplicationException ex) {
            assertThat(ex.getResponse().getStatus(), is(401));
            assertThat(ex.getResponse().getEntity().toString(), is("{\"error\":\"Invalid credentials\"}"));
        }
    }

    @Test
    public void shouldThrowUnauthorizedWebApplicationExceptionIfWrongAuthSchemeUsed() {
        try {
            when(webContext.getHeaderValue(HttpHeaders.AUTHORIZATION)).thenReturn("Basic something:something");
            when(webContext.getMethod()).thenReturn("GET");
            when(webContext.getPath()).thenReturn("http://localhost/test/path");
            when(webContext.getHeaderValue("X-Real-IP")).thenReturn("192.0.0.1");

            auth.getValue(context);
            fail("Should have thrown an unauthorized web exception");
        } catch (WebApplicationException ex) {
            assertThat(ex.getResponse().getStatus(), is(401));
            assertThat(ex.getResponse().getEntity().toString(), is("{\"error\":\"Invalid credentials\"}"));
        }
    }

    @Test
    public void shouldThrowUnauthorizedWebApplicationExceptionIfTokenNotFoundInDatabase() throws AuthenticationException {
        when(webContext.getHeaderValue(HttpHeaders.AUTHORIZATION)).thenReturn("Basic something:something");
        when(webContext.getMethod()).thenReturn("GET");
        when(webContext.getPath()).thenReturn("http://localhost/test/path");
        when(webContext.getHeaderValue("X-Real-IP")).thenReturn("192.0.0.1");
        when(mockAuthenticator.authenticate("good")).thenReturn(Optional.<AuthorizationToken>absent());

        try {
            auth.getValue(context);
            fail("Should have thrown an unauthorized web exception");
        } catch (WebApplicationException ex) {
            assertThat(ex.getResponse().getStatus(), is(401));
            assertThat(ex.getResponse().getEntity().toString(), is("{\"error\":\"Invalid credentials\"}"));
        }
    }

    @Test
    public void shouldDisallowIfExceedUsage() throws AuthenticationException {
        Usage exceededRate = new Usage("id", "identifier", 3, new Date());
        when(usageDao.findUsageByIdentifier("identifier")).thenReturn(Optional.of(exceededRate));
        when(configuration.getMaxRequestsPerDay()).thenReturn(2);
        when(mockAuthenticator.authenticate("good")).thenReturn(Optional.of(new AuthorizationToken("1", "name", "identifier", "organisation", "token", QueryType.ALL, DataType.ALL)));
        when(webContext.getHeaderValue(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer good");
        when(webContext.getPath()).thenReturn("path/to/resource");
        when(webContext.getMethod()).thenReturn("GET");
        try {
            auth.getValue(context);
        } catch (WebApplicationException ex) {
            assertThat(ex.getResponse().getStatus(), is(429));
            assertThat(ex.getResponse().getEntity().toString(), is("{\"error\":\"Exceed usage limits\"}"));
            assertThat(ex.getResponse().getMetadata().get("X-Locate-Limit-Max").size(), is(1));
            assertThat(ex.getResponse().getMetadata().get("X-Locate-Limit-Max").get(0).toString(), is("2"));
            assertThat(ex.getResponse().getMetadata().get("X-Locate-Limit-Used").size(), is(1));
            assertThat(ex.getResponse().getMetadata().get("X-Locate-Limit-Used").get(0).toString(), is("3"));
        }
    }

    @Test
    public void shouldAllowIfExactlyOnMaxUsageAllowed() throws AuthenticationException {
        Usage bangOnRate = new Usage("id", "identifier", 2, new Date());
        when(usageDao.findUsageByIdentifier("identifier")).thenReturn(Optional.of(bangOnRate));
        when(configuration.getMaxRequestsPerDay()).thenReturn(2);
        when(mockAuthenticator.authenticate("good")).thenReturn(Optional.of(new AuthorizationToken("1", "name", "identifier", "organisation", "token", QueryType.ALL, DataType.ALL)));
        when(webContext.getHeaderValue(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer good");
        when(webContext.getPath()).thenReturn("path/to/resource");
        when(webContext.getMethod()).thenReturn("GET");
        AuthorizationToken authorizationToken = auth.getValue(context);
        assertThat(authorizationToken.getIdentifier(), is("identifier"));
    }

    @Test
    public void shouldAllowAValidHttpRequestWithUsageUnderTheMaximum() throws AuthenticationException {
        Usage notExceededRate = new Usage("id", "identifier", 0, new Date());
        when(usageDao.findUsageByIdentifier("identifier")).thenReturn(Optional.of(notExceededRate));
        when(mockAuthenticator.authenticate("good")).thenReturn(Optional.of(new AuthorizationToken("1", "name", "identifier", "organisation", "token", QueryType.ALL, DataType.ALL)));
        when(webContext.getHeaderValue(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer good");
        when(webContext.getPath()).thenReturn("path/to/resource");
        when(webContext.getMethod()).thenReturn("GET");
        AuthorizationToken authorizationToken = auth.getValue(context);
        assertThat(authorizationToken.getIdentifier(), is("identifier"));
    }

    @Test
    public void shouldCreateAUsageRecordIfNoneExists() throws AuthenticationException {
        when(usageDao.findUsageByIdentifier("identifier")).thenReturn(Optional.<Usage>absent());
        when(mockAuthenticator.authenticate("good")).thenReturn(Optional.of(new AuthorizationToken("1", "name", "identifier", "organisation", "token", QueryType.ALL, DataType.ALL)));
        when(webContext.getHeaderValue(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer good");
        when(webContext.getPath()).thenReturn("path/to/resource");
        when(webContext.getMethod()).thenReturn("GET");
        AuthorizationToken authorizationToken = auth.getValue(context);
        assertThat(authorizationToken.getIdentifier(), is("identifier"));
        verify(usageDao, times(1)).create("identifier");
    }
}
