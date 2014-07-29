package uk.gov.gds.locate.api.resources;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import uk.gov.gds.locate.api.configuration.LocateApiConfiguration;
import uk.gov.gds.locate.api.dao.AddressDao;
import uk.gov.gds.locate.api.exceptions.LocateWebException;
import uk.gov.gds.locate.api.model.Address;
import uk.gov.gds.locate.api.model.AuthorizationToken;
import uk.gov.gds.locate.api.validation.ValidatePostcodes;

import javax.annotation.concurrent.Immutable;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Collections;
import java.util.List;

import static uk.gov.gds.locate.api.model.DataType.*;
import static uk.gov.gds.locate.api.services.AddressTransformationService.*;

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

        if (!ValidatePostcodes.isValid(postcode)) {
            throw new LocateWebException(422, ImmutableMap.of("error", "postcode is invalid"));
        }

        List<Address> addresses = getAddressesFromDb(postcode);
        List<Address> filtered = orderAddresses(applyPredicate(addresses, authorizationToken.getQueryType().predicate()));

        if (authorizationToken.getDataType().equals(ALL)) {
            return buildResponse().entity(filtered).build();
        }

        return buildResponse().entity(addressToSimpleAddress(filtered)).build();
    }

    private List<Address> getAddressesFromDb(String postcode) {
        if (configuration.getEncrypted()) {
            return decryptAddresses(addressDao.findAllForPostcode(postcode), configuration.getEncryptionKey());
        } else {
            return addressDao.findAllForPostcode(postcode);
        }
    }

    private Response.ResponseBuilder buildResponse() {
        return Response.ok().header("Content-type", "application/json");
    }
}
