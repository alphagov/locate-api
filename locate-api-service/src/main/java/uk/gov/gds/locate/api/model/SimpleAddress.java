package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleAddress {

    @JsonProperty("gssCode")
    private String gssCode;

    @JsonProperty("uprn")
    private String uprn;

    @JsonProperty("property")
    private String property;

    @JsonProperty("street")
    private String street;

    @JsonProperty("locality")
    private String locality;

    @JsonProperty("town")
    private String town;

    @JsonProperty("area")
    private String area;

    @JsonProperty("postcode")
    private String postcode;

    public SimpleAddress() {
    }

    public SimpleAddress(Address address) {
        this.gssCode = address.getGssCode();
        this.uprn = address.getUprn();
        this.property = address.getPresentation().getProperty();
        this.street = address.getPresentation().getStreet();
        this.locality = address.getPresentation().getLocality();
        this.town = address.getPresentation().getTown();
        this.area = address.getPresentation().getArea();
        this.postcode = address.getPresentation().getPostcode();
    }

    public String getGssCode() {
        return gssCode;
    }

    public String getUprn() {
        return uprn;
    }

    public String getProperty() {
        return property;
    }

    public String getStreet() {
        return street;
    }

    public String getLocality() {
        return locality;
    }

    public String getTown() {
        return town;
    }

    public String getArea() {
        return area;
    }

    public String getPostcode() {
        return postcode;
    }

    @Override
    public String toString() {
        return "SimpleAddress{" +
                "gssCode='" + gssCode + '\'' +
                ", uprn='" + uprn + '\'' +
                ", property='" + property + '\'' +
                ", street='" + street + '\'' +
                ", locality='" + locality + '\'' +
                ", town='" + town + '\'' +
                ", area='" + area + '\'' +
                ", postcode='" + postcode + '\'' +
                '}';
    }
}
