package uk.gov.gds.locate.api.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import org.mongojack.JacksonDBCollection;
import org.mongojack.internal.stream.JacksonDBObject;
import uk.gov.gds.locate.api.model.AuthorizationToken;

import java.util.Set;

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

    @Timed
    public void applyIndexes() {
        authorizationTokens.ensureIndex(new BasicDBObject("token", 1), "token_index", true);
        authorizationTokens.ensureIndex(new BasicDBObject("identifier", 1), "identifier_index", true);
    }
}
