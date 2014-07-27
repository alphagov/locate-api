package uk.gov.gds.locate.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import uk.gov.gds.locate.api.encryption.AesEncryptionService;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ordering implements Comparable<Ordering> {

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

    public Ordering decrypt(String key, String iv) {
        try {
            return new Ordering(
                    this.saoStartNumber,
                    this.saoStartSuffix,
                    this.saoEndNumber,
                    this.saoEndSuffix,
                    this.paoStartNumber,
                    this.paoStartSuffix,
                    this.paoEndNumber,
                    this.paoEndSuffix,
                    Strings.isNullOrEmpty(this.paoText) ? this.paoText : AesEncryptionService.decrypt(this.paoText, key, iv),
                    Strings.isNullOrEmpty(this.saoText) ? this.saoText : AesEncryptionService.decrypt(this.saoText, key, iv),
                    Strings.isNullOrEmpty(this.street) ? this.street : AesEncryptionService.decrypt(this.street, key, iv)
            );
        } catch (Exception e) {
            return this;
        }
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

    @Override
    public int compareTo(Ordering o) {
        return ComparisonChain.start().
                compare(this.saoStartNumber, o.getSaoStartNumber(), com.google.common.collect.Ordering.natural().nullsLast()).
                compare(this.saoStartSuffix, o.getSaoStartSuffix(), com.google.common.collect.Ordering.natural().nullsLast()).
                compare(this.saoEndNumber, o.getSaoEndNumber(), com.google.common.collect.Ordering.natural().nullsLast()).
                compare(this.saoEndSuffix, o.getSaoEndSuffix(), com.google.common.collect.Ordering.natural().nullsLast()).
                compare(this.paoStartNumber, o.getPaoStartNumber(), com.google.common.collect.Ordering.natural().nullsLast()).
                compare(this.paoStartSuffix, o.getPaoStartSuffix(), com.google.common.collect.Ordering.natural().nullsLast()).
                compare(this.paoEndNumber, o.getPaoEndNumber(), com.google.common.collect.Ordering.natural().nullsLast()).
                compare(this.paoEndSuffix, o.getPaoEndSuffix(), com.google.common.collect.Ordering.natural().nullsLast()).
                compare(this.saoText, o.getSaoText(), com.google.common.collect.Ordering.natural().nullsLast()).
                compare(this.paoText, o.getPaoText(), com.google.common.collect.Ordering.natural().nullsLast()).
                compare(this.street, o.getStreet(), com.google.common.collect.Ordering.natural().nullsLast()).result();
    }
}
