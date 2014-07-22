package uk.gov.gds.locate.api.resources;


import com.yammer.metrics.annotation.Timed;
import uk.gov.gds.locate.api.views.CompleteView;
import uk.gov.gds.locate.api.views.CreateUserView;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/locate/create-user")
@Produces(MediaType.TEXT_HTML)
public class CreateUserResource {

    @GET
    @Timed
    public CreateUserView createUser() {
        return new CreateUserView();
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public CompleteView createUser(
            @FormParam("name") String name,
            @FormParam("email") String email,
            @FormParam("organisation") String organisation,
            @FormParam("query_type") String queryType,
            @FormParam("data_type") String dataType

    ) throws Exception {
        return new CompleteView();
    }

}
