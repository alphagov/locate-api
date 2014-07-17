package uk.gov.gds.locate.api.dao;

import com.mongodb.BasicDBObject;
import org.mongojack.JacksonDBCollection;
import uk.gov.gds.locate.api.model.AuthorizationToken;

public class AuthorizationTokenDao {

    private final JacksonDBCollection<AuthorizationToken, String> authorizationTokens;

    public AuthorizationTokenDao(JacksonDBCollection<AuthorizationToken, String> authorizationTokens) {
        this.authorizationTokens = authorizationTokens;
    }

    public AuthorizationToken fetchCredentialsByBearerToken(String token) {
        return authorizationTokens.findOne(new BasicDBObject("token", token));
    }
}
