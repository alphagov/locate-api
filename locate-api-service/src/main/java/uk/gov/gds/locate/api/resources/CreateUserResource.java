package uk.gov.gds.locate.api.resources;

import com.yammer.metrics.annotation.Timed;
import uk.gov.gds.locate.api.dao.AuthorizationTokenDao;
import uk.gov.gds.locate.api.exceptions.LocateWebException;
import uk.gov.gds.locate.api.model.AuthorizationToken;
import uk.gov.gds.locate.api.model.CreateUserRequest;
import uk.gov.gds.locate.api.model.QueryType;
import uk.gov.gds.locate.api.services.BearerTokenGenerationService;
import uk.gov.gds.locate.api.views.CompleteView;
import uk.gov.gds.locate.api.views.CreateUserView;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static uk.gov.gds.locate.api.validation.ValidationCreateUserRequest.validateRequest;

@Path("/locate/create-user")
public class CreateUserResource {

    private final AuthorizationTokenDao authorizationTokenDao;
    private final BearerTokenGenerationService bearerTokenGenerationService;

    public CreateUserResource(AuthorizationTokenDao authorizationTokenDao, BearerTokenGenerationService bearerTokenGenerationService) {
        this.authorizationTokenDao = authorizationTokenDao;
        this.bearerTokenGenerationService = bearerTokenGenerationService;
    }

    @GET
    @Timed
    @Produces(MediaType.TEXT_HTML)
    public CreateUserView createUser() {
        return new CreateUserView();
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AuthorizationToken createUserFromJson(CreateUserRequest request) throws Exception {

        List<String> errors = validateRequest(request);

        if (!errors.isEmpty()) {
            throw new LocateWebException(422, errors);
        }

        AuthorizationToken token = new AuthorizationToken(
                org.bson.types.ObjectId.get().toString(),
                request.getEmail(),
                bearerTokenGenerationService.newToken(),
                QueryType.parse(request.getQueryType())
        );

        authorizationTokenDao.create(token);
        return token;
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public CompleteView createUserFromForm(
            @FormParam("email") String email,
            @FormParam("name") String name,
            @FormParam("organisation") String organisation,
            @FormParam("queryType") String queryType,
            @FormParam("dataType") String dataType
    ) throws Exception {
        CreateUserRequest request = new CreateUserRequest(name, email, organisation, queryType, dataType);

        List<String> errors = validateRequest(request);

        if (errors.size() == 0) {
            AuthorizationToken token = new AuthorizationToken(
                    org.bson.types.ObjectId.get().toString(),
                    request.getEmail(),
                    bearerTokenGenerationService.newToken(),
                    QueryType.parse(request.getQueryType())
            );
            authorizationTokenDao.create(token);
            return new CompleteView(token, errors);
        }
        return new CompleteView(new AuthorizationToken(), errors);
    }

}
