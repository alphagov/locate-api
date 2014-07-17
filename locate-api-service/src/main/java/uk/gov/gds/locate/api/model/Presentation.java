package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Presentation {

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

    public Presentation() {
    }

    public Presentation(String property, String street, String locality, String town, String area, String postcode) {
        this.property = property;
        this.street = street;
        this.locality = locality;
        this.town = town;
        this.area = area;
        this.postcode = postcode;
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
}
