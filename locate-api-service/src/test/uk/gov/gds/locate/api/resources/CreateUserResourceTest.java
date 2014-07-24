package uk.gov.gds.locate.api.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Test;
import uk.gov.gds.locate.api.LocateExceptionMapper;
import uk.gov.gds.locate.api.dao.AuthorizationTokenDao;
import uk.gov.gds.locate.api.model.AuthorizationToken;
import uk.gov.gds.locate.api.model.CreateUserRequest;
import uk.gov.gds.locate.api.services.BearerTokenGenerationService;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CreateUserResourceTest extends ResourceTest {

    private AuthorizationTokenDao dao = mock(AuthorizationTokenDao.class);
    private BearerTokenGenerationService bearerTokenGenerationService = mock(BearerTokenGenerationService.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldCreateAUserAccountForAValidRequest() throws JsonProcessingException {
        when(bearerTokenGenerationService.newToken()).thenReturn("this is a token");
        when(dao.create(any(AuthorizationToken.class))).thenReturn(true);
        CreateUserRequest request = new CreateUserRequest("name", "real@email.gov.uk", "org", "all", "all");

        String jsonRequest = mapper.writeValueAsString(request);
        String response = client().resource("/locate/create-user").header("Content-type", "application/json").post(String.class, jsonRequest);
        assertThat(response).contains("\"identifier\":\"real@email.gov.uk\"");
        assertThat(response).contains("\"token\":\"this is a token\"");
        verify(dao, times(1)).create(any(AuthorizationToken.class));
    }

    @Test
    public void shouldRejectAnInvalidCreateRequest() throws JsonProcessingException {
        when(dao.create(any(AuthorizationToken.class))).thenReturn(true);
        CreateUserRequest request = new CreateUserRequest("", "real@email.gov.uk", "org", "all", "all");

        String jsonRequest = mapper.writeValueAsString(request);
        try {
            client().resource("/locate/create-user").header("Content-type", "application/json").post(String.class, jsonRequest);
            fail("Should have thrown exception");
        } catch (UniformInterfaceException e) {
            verify(dao, times(0)).create(any(AuthorizationToken.class));
            assertThat(e.getResponse().getStatus()).isEqualTo(422);
            assertThat(e.getResponse().getEntity(String.class)).isEqualTo("[\"Name must be present and shorter than 255 letters\"]");
        }
    }


    @Override
    protected void setUpResources() throws Exception {
        addResource(new CreateUserResource(dao, bearerTokenGenerationService));
        addProvider(new LocateExceptionMapper());
    }
}