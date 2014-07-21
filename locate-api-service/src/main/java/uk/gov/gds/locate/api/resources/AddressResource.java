package uk.gov.gds.locate.api.resources;

import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gds.locate.api.dao.AddressDao;
import uk.gov.gds.locate.api.model.Address;
import uk.gov.gds.locate.api.model.AuthorizationToken;
import uk.gov.gds.locate.api.model.SimpleAddress;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static uk.gov.gds.locate.api.services.AddressTransformationService.addressToSimpleAddress;

@Path("/locate/addresses")
@Produces(MediaType.APPLICATION_JSON)
public class AddressResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressResource.class);

    private final AddressDao addressDao;

    public AddressResource(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @GET
    @Timed
    public List<Address> fetchAddresses(@Auth AuthorizationToken authorizationToken, @QueryParam("postcode") String postcode) throws Exception {
       // return addressToSimpleAddress(addressDao.findAllForPostcode(postcode));
       return addressDao.findAllForPostcode(postcode);
    }
}
