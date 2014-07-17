package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {

    @JsonProperty("gssCode")
    private String gssCode;

    @JsonProperty("uprn")
    private String uprn;

    @JsonProperty("presentation")
    private Presentation presentation;

    public Address() {
    }

    public Address(String gssCode, String uprn, Presentation presentation) {
        this.gssCode = gssCode;
        this.uprn = uprn;
        this.presentation = presentation;
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
}
