package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.xml.soap.Detail;
import java.util.Date;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address implements Comparable<Address> {

    private String iv;

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


    public Address(String gssCode, String uprn, String postcode, String country, Date createdAt, Presentation presentation, Details details, Location location, Ordering ordering, String iv) {
        this.gssCode = gssCode;
        this.uprn = uprn;
        this.postcode = postcode;
        this.country = country;
        this.createdAt = createdAt;
        this.presentation = presentation;
        this.details = details;
        this.location = location;
        this.ordering = ordering;
        this.iv = iv;
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

    @JsonIgnore
    public String getIv() {
        return iv;
    }

    @JsonProperty("iv")
    public void setIv(String iv) {
        this.iv = iv;
    }

    public Address decrypt(String key, String iv) {
        return new Address(
                this.gssCode,
                this.uprn,
                this.postcode,
                this.country,
                this.createdAt,
                this.presentation.decrypt(key, iv),
                this.details,
                this.location,
                this.ordering.decrypt(key, iv),
                this.iv
        );
    }

    @Override
    public String toString() {
        return "Address{" +
                "iv='" + iv + '\'' +
                ", gssCode='" + gssCode + '\'' +
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

    @Override
    public int compareTo(Address o) {
        return this.ordering.compareTo(o.getOrdering());
    }
}
