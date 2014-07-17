package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import static uk.gov.gds.locate.api.authentication.Obfuscated.getObfuscatedToken;

public class AuthorizationToken {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("token")
    private String token;


    public AuthorizationToken() {
    }

    public AuthorizationToken(String id, String identifier, String token) {
        this.id = id;
        this.identifier = identifier;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "AuthorizationToken{" +
                "id='" + id + '\'' +
                ", token='" + getObfuscatedToken(token) + '\'' +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}