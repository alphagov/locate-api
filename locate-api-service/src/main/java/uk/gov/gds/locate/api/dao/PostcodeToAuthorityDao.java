package uk.gov.gds.locate.api.dao;


import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.yammer.metrics.annotation.Timed;
import org.mongojack.JacksonDBCollection;
import uk.gov.gds.locate.api.model.PostcodeToAuthority;

import java.util.List;

public class PostcodeToAuthorityDao {

    private final JacksonDBCollection<PostcodeToAuthority, String> authorities;

    public PostcodeToAuthorityDao(JacksonDBCollection<PostcodeToAuthority, String> authorities) {
        this.authorities = authorities;
    }

    @Timed
    public PostcodeToAuthority findForPostcode(String postcode) {
        return authorities.findOne(new BasicDBObject("postcode", postcode));
    }
}
