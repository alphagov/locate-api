package uk.gov.gds.locate.api.resources;

import com.google.common.collect.ImmutableMap;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import uk.gov.gds.locate.api.dao.PostcodeToAuthorityDao;
import uk.gov.gds.locate.api.exceptions.LocateWebException;
import uk.gov.gds.locate.api.model.AuthorizationToken;
import uk.gov.gds.locate.api.model.PostcodeToAuthority;
import uk.gov.gds.locate.api.validation.ValidatePostcodes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/locate/authority")
@Produces(MediaType.APPLICATION_JSON)
public class PostcodeToAuthorityResource {

    private final PostcodeToAuthorityDao dao;

    public PostcodeToAuthorityResource(PostcodeToAuthorityDao dao) {
        this.dao = dao;
    }

    @GET
    @Timed
    public PostcodeToAuthority fetchAddresses(@Auth AuthorizationToken authorizationToken, @QueryParam("postcode") String postcode) throws Exception {
        if (!ValidatePostcodes.isValid(postcode)) {
            throw new LocateWebException(422, ImmutableMap.of("error", "postcode is invalid"));
        }
        return dao.findForPostcode(postcode);
    }
}
