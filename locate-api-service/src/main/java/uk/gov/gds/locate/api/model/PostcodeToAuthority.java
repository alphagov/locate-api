package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostcodeToAuthority {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("gssCode")
    private String gssCode;

    @JsonProperty("country")
    private String country;

    @JsonProperty("postcode")
    private String postcode;

    @JsonProperty("name")
    private String name;

    public PostcodeToAuthority() {
    }

    public PostcodeToAuthority(String id, String gssCode, String country, String postcode, String name) {
        this.id = id;
        this.gssCode = gssCode;
        this.country = country;
        this.postcode = postcode;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getGssCode() {
        return gssCode;
    }

    public String getCountry() {
        return country;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "PostcodeToAuthority{" +
                "id='" + id + '\'' +
                ", gssCode='" + gssCode + '\'' +
                ", country='" + country + '\'' +
                ", postcode='" + postcode + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
