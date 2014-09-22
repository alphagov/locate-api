package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.mongojack.ObjectId;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationToken {

    @JsonProperty("_id")
    @ObjectId
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("organisation")
    private String organisation;

    @JsonProperty("token")
    private String token;

    public AuthorizationToken() {
    }

    public AuthorizationToken(String id, String name, String identifier, String organisation, String token) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.organisation = organisation;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "AuthorizationToken{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", identifier='" + identifier + '\'' +
                ", organisation='" + organisation + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}