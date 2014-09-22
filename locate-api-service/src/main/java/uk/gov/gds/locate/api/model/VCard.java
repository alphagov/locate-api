package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


import com.google.common.base.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VCard {

    @JsonProperty("extended-address")
    private String extendedAddress;

    @JsonProperty("street-address")
    private String streetAddress;

    @JsonProperty("locality")
    private String locality;

    @JsonProperty("region")
    private String region;

    @JsonProperty("postal-code")
    private String postalCode;

    @JsonProperty("country")
    private String country;

    @JsonProperty("x-uprn")
    private String uprn;

    @JsonProperty("vcard")
    private String vcard;

    public VCard() {
    }

    public VCard(Address address) {
        this(
                address.getPresentation().getProperty(),
                address.getPresentation().getStreet(),
                address.getPresentation().getTown(),
                address.getPresentation().getArea(),
                address.getPresentation().getPostcode(),
                address.getUprn(),
                address.getCountry()
        );
    }

    public VCard(String extendedAddress, String streetAddress, String locality, String region, String postalCode, String uprn, String country) {
        this.extendedAddress = extendedAddress;
        this.streetAddress = streetAddress;
        this.locality = locality;
        this.region = region;
        this.postalCode = postalCode;
        this.uprn = uprn;
        this.country = country;
        this.vcard = String.format("ADR;:;;%s;%s;%s;%s;%s;%s",
                Objects.firstNonNull(extendedAddress, ""),
                Objects.firstNonNull(streetAddress, ""),
                Objects.firstNonNull(locality, ""),
                Objects.firstNonNull(region, ""),
                Objects.firstNonNull(postalCode, ""),
                Objects.firstNonNull(country, "")
        );
    }

    public String getExtendedAddress() {
        return extendedAddress;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getLocality() {
        return locality;
    }

    public String getRegion() {
        return region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getUprn() {
        return uprn;
    }

    public String getVcard() {
        return vcard;
    }


    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "VCard{" +
                "extendedAddress='" + extendedAddress + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", locality='" + locality + '\'' +
                ", region='" + region + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                ", uprn='" + uprn + '\'' +
                ", vcard='" + vcard + '\'' +
                '}';
    }
}