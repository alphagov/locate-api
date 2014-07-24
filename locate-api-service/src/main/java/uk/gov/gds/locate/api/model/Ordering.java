package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ordering {

    @JsonProperty("saoStartNumber")
    private String saoStartNumber;

    @JsonProperty("saoStartSuffix")
    private String saoStartSuffix;


    @JsonProperty("saoEndNumber")
    private String saoEndNumber;

    @JsonProperty("saoEndSuffix")
    private String saoEndSuffix;

    @JsonProperty("paoStartNumber")
    private String paoStartNumber;

    @JsonProperty("paoStartSuffix")
    private String paoStartSuffix;

    @JsonProperty("paoEndNumber")
    private String paoEndNumber;

    @JsonProperty("paoEndSuffix")
    private String paoEndSuffix;

    @JsonProperty("paoText")
    private String paoText;

    @JsonProperty("saoText")
    private String saoText;

    @JsonProperty("street")
    private String street;

    public Ordering() {
    }

    public Ordering(String saoStartNumber, String saoStartSuffix, String saoEndNumber, String saoEndSuffix, String paoStartNumber, String paoStartSuffix, String paoEndNumber, String paoEndSuffix, String paoText, String saoText, String street) {
        this.saoStartNumber = saoStartNumber;
        this.saoStartSuffix = saoStartSuffix;
        this.saoEndNumber = saoEndNumber;
        this.saoEndSuffix = saoEndSuffix;
        this.paoStartNumber = paoStartNumber;
        this.paoStartSuffix = paoStartSuffix;
        this.paoEndNumber = paoEndNumber;
        this.paoEndSuffix = paoEndSuffix;
        this.paoText = paoText;
        this.saoText = saoText;
        this.street = street;
    }

    public String getSaoStartNumber() {
        return saoStartNumber;
    }

    public String getSaoStartSuffix() {
        return saoStartSuffix;
    }

    public String getSaoEndNumber() {
        return saoEndNumber;
    }

    public String getSaoEndSuffix() {
        return saoEndSuffix;
    }

    public String getPaoStartNumber() {
        return paoStartNumber;
    }

    public String getPaoStartSuffix() {
        return paoStartSuffix;
    }

    public String getPaoEndNumber() {
        return paoEndNumber;
    }

    public String getPaoEndSuffix() {
        return paoEndSuffix;
    }

    public String getPaoText() {
        return paoText;
    }

    public String getSaoText() {
        return saoText;
    }

    public String getStreet() {
        return street;
    }

    @Override
    public String toString() {
        return "Ordering{" +
                "saoStartNumber='" + saoStartNumber + '\'' +
                ", saoStartSuffix='" + saoStartSuffix + '\'' +
                ", saoEndNumber='" + saoEndNumber + '\'' +
                ", saoEndSuffix='" + saoEndSuffix + '\'' +
                ", paoStartNumber='" + paoStartNumber + '\'' +
                ", paoStartSuffix='" + paoStartSuffix + '\'' +
                ", paoEndNumber='" + paoEndNumber + '\'' +
                ", paoEndSuffix='" + paoEndSuffix + '\'' +
                ", paoText='" + paoText + '\'' +
                ", saoText='" + saoText + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}
