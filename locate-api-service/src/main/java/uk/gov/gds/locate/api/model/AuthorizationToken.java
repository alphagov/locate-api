package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationToken {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("token")
    private String token;

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "AuthorizationToken{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}