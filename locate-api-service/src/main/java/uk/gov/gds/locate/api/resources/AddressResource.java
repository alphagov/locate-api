package uk.gov.gds.locate.api.resources;

import com.yammer.metrics.annotation.Timed;
import uk.gov.gds.locate.api.dao.AddressDao;
import uk.gov.gds.locate.api.model.Address;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/addresses")
@Produces(MediaType.APPLICATION_JSON)
public class AddressResource {

    private final AddressDao addressDao;

    public AddressResource(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @GET
    @Timed
    public List<Address> fetchAddresses(@QueryParam("postcode") String postcode) throws Exception {
        return addressDao.findAllForPostcode(postcode);
    }
}
