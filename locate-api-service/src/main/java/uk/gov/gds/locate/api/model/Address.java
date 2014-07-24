package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.soap.Detail;
import java.util.Date;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address implements BaseAddress {

    @JsonProperty("gssCode")
    private String gssCode;

    @JsonProperty("uprn")
    private String uprn;

    @JsonProperty("postcode")
    private String postcode;

    @JsonProperty("country")
    private String country;

    @JsonProperty("createdAt")
    private Date createdAt;

    @JsonProperty("presentation")
    private Presentation presentation;

    @JsonProperty("details")
    private Details details;

    @JsonProperty("location")
    private Location location;

    @JsonProperty("ordering")
    private Ordering ordering;

    public Address() {
    }


    public Address(String gssCode, String uprn, String postcode, String country, Date createdAt, Presentation presentation, Details details, Location location, Ordering ordering) {
        this.gssCode = gssCode;
        this.uprn = uprn;
        this.postcode = postcode;
        this.country = country;
        this.createdAt = createdAt;
        this.presentation = presentation;
        this.details = details;
        this.location = location;
        this.ordering = ordering;
    }

    public String getGssCode() {
        return gssCode;
    }

    public String getUprn() {
        return uprn;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCountry() {
        return country;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public Details getDetails() {
        return details;
    }

    public Location getLocation() {
        return location;
    }

    public Ordering getOrdering() {
        return ordering;
    }

    @Override
    public String toString() {
        return "Address{" +
                "gssCode='" + gssCode + '\'' +
                ", uprn='" + uprn + '\'' +
                ", postcode='" + postcode + '\'' +
                ", country='" + country + '\'' +
                ", createdAt=" + createdAt +
                ", presentation=" + presentation +
                ", details=" + details +
                ", location=" + location +
                ", ordering=" + ordering +
                '}';
    }
}
