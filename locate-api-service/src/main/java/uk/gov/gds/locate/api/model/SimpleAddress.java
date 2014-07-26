package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import uk.gov.gds.locate.api.encryption.AesEncryptionService;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleAddress implements BaseAddress {

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

    public SimpleAddress(String gssCode, String uprn, String property, String street, String locality, String town, String area, String postcode) {
        this.gssCode = gssCode;
        this.uprn = uprn;
        this.property = property;
        this.street = street;
        this.locality = locality;
        this.town = town;
        this.area = area;
        this.postcode = postcode;
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

    public SimpleAddress decrypt(String key, String iv) {
        try {
            return new SimpleAddress(
                    this.gssCode,
                    this.uprn,
                    Strings.isNullOrEmpty(this.property) ? this.property : AesEncryptionService.decrypt(this.property, key, iv),
                    Strings.isNullOrEmpty(this.street) ? this.street : AesEncryptionService.decrypt(this.street, key, iv),
                    Strings.isNullOrEmpty(this.locality) ? this.locality : AesEncryptionService.decrypt(this.locality, key, iv),
                    Strings.isNullOrEmpty(this.town) ? this.town : AesEncryptionService.decrypt(this.town, key, iv),
                    Strings.isNullOrEmpty(this.area) ? this.area : AesEncryptionService.decrypt(this.area, key, iv),
                    this.postcode
            );
        } catch (Exception e) {
            return this;
        }
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
