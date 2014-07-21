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

    @JsonProperty("requests")
    private Integer requests;

    public AuthorizationToken() {
    }

    public AuthorizationToken(String id, String identifier, String token, Integer requests) {
        this.id = id;
        this.identifier = identifier;
        this.token = token;
        this.requests = requests;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getToken() {
        return token;
    }

    public Integer getRequests() {
        return requests;
    }

    @Override
    public String toString() {
        return "AuthorizationToken{" +
                "id='" + id + '\'' +
                ", identifier='" + identifier + '\'' +
                ", token='" + token + '\'' +
                ", requests=" + requests +
                '}';
    }
}