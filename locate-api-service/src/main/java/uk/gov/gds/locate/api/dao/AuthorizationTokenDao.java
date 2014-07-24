package uk.gov.gds.locate.api.dao;

import com.mongodb.BasicDBObject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import org.mongojack.JacksonDBCollection;
import uk.gov.gds.locate.api.model.AuthorizationToken;

public class AuthorizationTokenDao {

    private final JacksonDBCollection<AuthorizationToken, String> authorizationTokens;

    public AuthorizationTokenDao(JacksonDBCollection<AuthorizationToken, String> authorizationTokens) {
        this.authorizationTokens = authorizationTokens;
    }

    @Timed
    public AuthorizationToken fetchCredentialsByBearerToken(String token) {
        return authorizationTokens.findOne(new BasicDBObject("token", token));
    }

    @Timed
    public Boolean create(AuthorizationToken authorizationToken) {
        return authorizationTokens.insert(authorizationToken).getN() == 1;
    }
}
