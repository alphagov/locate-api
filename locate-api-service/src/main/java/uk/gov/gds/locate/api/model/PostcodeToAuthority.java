package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostcodeToAuthority {

    @JsonIgnore
    private String id;

    @JsonProperty("gssCode")
    private String gssCode;

    @JsonProperty("country")
    private String country;

    @JsonProperty("postcode")
    private String postcode;

    @JsonProperty("name")
    private String name;

    @JsonProperty("easting")
    private Double easting;

    @JsonProperty("northing")
    private Double northing;

    @JsonProperty("lat")
    private Double latitude;

    @JsonProperty("long")
    private Double longitude;

    @JsonProperty("nhsRegionalHealthAuthority")
    private String nhsRegionalHealthAuthority;

    @JsonProperty("nhsHealthAuthority")
    private String nhsHealthAuthority;

    @JsonProperty("county")
    private String county;

    @JsonProperty("ward")
    private String ward;


    public PostcodeToAuthority() {
    }

    public PostcodeToAuthority(String id, String gssCode, String country, String postcode, String name, Double easting, Double northing, Double latitude, Double longitude, String nhsRegionalHealthAuthority, String nhsHealthAuthority, String county, String ward) {
        this.id = id;
        this.gssCode = gssCode;
        this.country = country;
        this.postcode = postcode;
        this.name = name;
        this.easting = easting;
        this.northing = northing;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nhsRegionalHealthAuthority = nhsRegionalHealthAuthority;
        this.nhsHealthAuthority = nhsHealthAuthority;
        this.county = county;
        this.ward = ward;
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

    public Double getEasting() {
        return easting;
    }

    public Double getNorthing() {
        return northing;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getNhsRegionalHealthAuthority() {
        return nhsRegionalHealthAuthority;
    }

    public String getNhsHealthAuthority() {
        return nhsHealthAuthority;
    }

    public String getCounty() {
        return county;
    }

    public String getWard() {
        return ward;
    }

    @Override
    public String toString() {
        return "PostcodeToAuthority{" +
                "id='" + id + '\'' +
                ", gssCode='" + gssCode + '\'' +
                ", country='" + country + '\'' +
                ", postcode='" + postcode + '\'' +
                ", name='" + name + '\'' +
                ", easting=" + easting +
                ", northing=" + northing +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", nhsRegionalHealthAuthority='" + nhsRegionalHealthAuthority + '\'' +
                ", nhsHealthAuthority='" + nhsHealthAuthority + '\'' +
                ", county='" + county + '\'' +
                ", ward='" + ward + '\'' +
                '}';
    }
}
