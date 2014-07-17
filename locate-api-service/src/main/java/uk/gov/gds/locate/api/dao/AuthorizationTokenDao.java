package uk.gov.gds.locate.api.dao;

import com.mongodb.BasicDBObject;
import org.mongojack.JacksonDBCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gds.locate.api.model.AuthorizationToken;

public class AuthorizationTokenDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationTokenDao.class);

    private final JacksonDBCollection<AuthorizationToken, String> authorizationTokens;

    public AuthorizationTokenDao(JacksonDBCollection<AuthorizationToken, String> authorizationTokens) {
        this.authorizationTokens = authorizationTokens;
    }

    public AuthorizationToken fetchCredentialsByBearerToken(String token) {
        LOGGER.info(" THIS " + authorizationTokens.findOne(new BasicDBObject("token", token)));
        return authorizationTokens.findOne(new BasicDBObject("token", token));
    }
}
