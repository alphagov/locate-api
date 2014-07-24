package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.soap.Detail;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address implements BaseAddress {

    @JsonProperty("gssCode")
    private String gssCode;

    @JsonProperty("uprn")
    private String uprn;

    @JsonProperty("presentation")
    private Presentation presentation;

    @JsonProperty("details")
    private Details details;

    @JsonProperty("location")
    private Location location;

    public Address() {
    }

    public Address(String gssCode, String uprn, Presentation presentation, Details details, Location location) {
        this.gssCode = gssCode;
        this.uprn = uprn;
        this.presentation = presentation;
        this.details = details;
        this.location = location;
    }

    public String getGssCode() {
        return gssCode;
    }

    public String getUprn() {
        return uprn;
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
}
