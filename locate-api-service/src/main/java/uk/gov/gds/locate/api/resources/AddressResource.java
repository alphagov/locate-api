package uk.gov.gds.locate.api.resources;


import com.yammer.dropwizard.assets.ResourceNotFoundException;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import uk.gov.gds.locate.api.model.Address;
import uk.gov.gds.locate.api.model.Credentials;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/addresses")
@Produces(MediaType.APPLICATION_JSON)
public class AddressResource {

    @GET
    @Timed
    public List<Address> fetchAddresses(@QueryParam("postcode") String postcode) throws Exception {
        throw new ResourceNotFoundException(new Exception("Not implemented"));
    }
}
