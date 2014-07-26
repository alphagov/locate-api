package uk.gov.gds.locate.api.resources;

import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import uk.gov.gds.locate.api.configuration.LocateApiConfiguration;
import uk.gov.gds.locate.api.dao.AddressDao;
import uk.gov.gds.locate.api.model.Address;
import uk.gov.gds.locate.api.model.AuthorizationToken;
import uk.gov.gds.locate.api.model.DataType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static uk.gov.gds.locate.api.model.DataType.*;
import static uk.gov.gds.locate.api.services.AddressTransformationService.addressToSimpleAddress;
import static uk.gov.gds.locate.api.services.AddressTransformationService.decryptAddressToSimpleAddress;
import static uk.gov.gds.locate.api.services.AddressTransformationService.filter;

@Path("/locate/addresses")
@Produces(MediaType.APPLICATION_JSON)
public class AddressResource {

    private final AddressDao addressDao;
    private final LocateApiConfiguration configuration;

    public AddressResource(AddressDao addressDao, LocateApiConfiguration configuration) {
        this.addressDao = addressDao;
        this.configuration = configuration;
    }

    @GET
    @Timed
    public Response fetchAddresses(@Auth AuthorizationToken authorizationToken, @QueryParam("postcode") String postcode) throws Exception {

        List<Address> addresses = filter(addressDao.findAllForPostcode(postcode), authorizationToken.getQueryType().predicate());
        if (authorizationToken.getDataType().equals(ALL)) {
            return buildResponse().entity(addresses).build();
        }

        //return buildResponse().entity(addressToSimpleAddress(addresses)).build();
        return buildResponse().entity(decryptAddressToSimpleAddress(addresses, configuration.getEncryptionKey())).build();
    }

    private Response.ResponseBuilder buildResponse() {
        return Response.ok().header("Content-type", "application/json");
    }
}
