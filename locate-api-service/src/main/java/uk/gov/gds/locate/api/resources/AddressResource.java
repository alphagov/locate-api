package uk.gov.gds.locate.api.resources;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import uk.gov.gds.locate.api.configuration.LocateApiConfiguration;
import uk.gov.gds.locate.api.dao.AddressDao;
import uk.gov.gds.locate.api.exceptions.LocateWebException;
import uk.gov.gds.locate.api.model.Address;
import uk.gov.gds.locate.api.model.AuthorizationToken;
import uk.gov.gds.locate.api.model.Format;
import uk.gov.gds.locate.api.model.QueryType;
import uk.gov.gds.locate.api.validation.ValidateFormat;
import uk.gov.gds.locate.api.validation.ValidatePostcodes;
import uk.gov.gds.locate.api.validation.ValidateQuery;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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
    public Response fetchAddresses(@Auth AuthorizationToken authorizationToken, @QueryParam("postcode") String postcode, @QueryParam("format") String format, @QueryParam("query") String query) throws Exception {

        if (!ValidatePostcodes.isValid(postcode)) {
            throw new LocateWebException(422, ImmutableMap.of("error", "postcode is invalid"));
        }

        if (!ValidateFormat.isValid(format)) {
            throw new LocateWebException(422, ImmutableMap.of("error", "format is invalid"));
        }

        if (!ValidateQuery.isValid(query)) {
            throw new LocateWebException(422, ImmutableMap.of("error", "query is invalid"));
        }

        List<Address> addresses = getAddressesFromDb(tidyPostcode(postcode));
        List<Address> filtered = orderAddresses(applyPredicate(addresses, QueryType.parse(query).predicate()));

        if (!Strings.isNullOrEmpty(format) && format.equals(Format.VCARD.getType())) {
            return buildResponse().entity(addressToVCard(filtered)).build();
        }

        if (!Strings.isNullOrEmpty(format) && format.equals(Format.ALL.getType())) {
            return buildResponse().entity(filtered).build();
        }

        return buildResponse().entity(addressToSimpleAddress(filtered)).build();
    }

    private String tidyPostcode(String postcode) {
        return postcode.toLowerCase().trim().replace(" ", "");
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
