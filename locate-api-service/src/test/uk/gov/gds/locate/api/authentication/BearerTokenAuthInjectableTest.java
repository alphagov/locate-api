package uk.gov.gds.locate.api.authentication;

import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;
import com.yammer.dropwizard.auth.AuthenticationException;
import org.junit.Before;
import org.junit.Test;
import uk.gov.gds.locate.api.configuration.LocateApiConfiguration;
import uk.gov.gds.locate.api.model.AuthorizationToken;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import static junit.framework.TestCase.fail;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BearerTokenAuthInjectableTest {

    private BearerTokenAuthenticator mockAuthenticator = mock(BearerTokenAuthenticator.class);
    private HttpContext context = mock(HttpContext.class);
    private HttpRequestContext webContext = mock(HttpRequestContext.class);
    private BearerTokenAuthInjectable auth;
    private LocateApiConfiguration configuration = mock(LocateApiConfiguration.class);

    @Before
    public void setUp() {
        when(context.getRequest()).thenReturn(webContext);
        when(configuration.getMaxRequestsPerDay()).thenReturn(1000);
        auth = new BearerTokenAuthInjectable(configuration, mockAuthenticator);
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
        when(mockAuthenticator.authenticate(any(BearerToken.class))).thenReturn(Optional.<AuthorizationToken>absent());

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
        when(configuration.getMaxRequestsPerDay()).thenReturn(2);
        when(mockAuthenticator.authenticate(any(BearerToken.class))).thenReturn(Optional.of(new AuthorizationToken("1", "identifier", "token", 2)));
        when(webContext.getHeaderValue(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer good");
        when(webContext.getPath()).thenReturn("path/to/resource");
        when(webContext.getMethod()).thenReturn("GET");
        try {
            auth.getValue(context);
        } catch (WebApplicationException ex) {
            assertThat(ex.getResponse().getStatus(), is(429));
            assertThat(ex.getResponse().getEntity().toString(), is("{\"error\":\"Exceeded usage\"}"));
        }
    }

    @Test
    public void shouldAllowIfExactlyOnUsage() throws AuthenticationException {
        when(configuration.getMaxRequestsPerDay()).thenReturn(2);
        when(mockAuthenticator.authenticate(any(BearerToken.class))).thenReturn(Optional.of(new AuthorizationToken("1", "identifier", "token", 2)));
        when(webContext.getHeaderValue(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer good");
        when(webContext.getPath()).thenReturn("path/to/resource");
        when(webContext.getMethod()).thenReturn("GET");
        AuthorizationToken authorizationToken = auth.getValue(context);
        assertThat(authorizationToken.getIdentifier(), is("identifier"));
    }

    @Test
    public void shouldAllowAValidHttpRequest() throws AuthenticationException {
        when(mockAuthenticator.authenticate(any(BearerToken.class))).thenReturn(Optional.of(new AuthorizationToken("1", "identifier", "token", 1)));
        when(webContext.getHeaderValue(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer good");
        when(webContext.getPath()).thenReturn("path/to/resource");
        when(webContext.getMethod()).thenReturn("GET");
        AuthorizationToken authorizationToken = auth.getValue(context);
        assertThat(authorizationToken.getIdentifier(), is("identifier"));
    }
}
