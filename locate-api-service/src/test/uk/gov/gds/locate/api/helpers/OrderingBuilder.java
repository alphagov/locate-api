package uk.gov.gds.locate.api.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.gds.locate.api.model.Ordering;
import uk.gov.gds.locate.api.model.Presentation;

public class OrderingBuilder {

    private String saoStartNumber;
    private String saoStartSuffix;
    private String saoEndNumber;
    private String saoEndSuffix;
    private String paoStartNumber;
    private String paoStartSuffix;
    private String paoEndNumber;
    private String paoEndSuffix;
    private String paoText;
    private String saoText;
    private String street;


    public OrderingBuilder(String suffix) {
        this.saoStartNumber = "saoStartNumber-" + suffix;
        this.saoStartSuffix = "saoStartSuffix-" + suffix;
        this.saoEndNumber = "saoEndNumber-" + suffix;
        this.saoEndSuffix = "saoEndSuffix-" + suffix;
        this.paoStartNumber = "paoStartNumber-" + suffix;
        this.paoStartSuffix = "paoStartSuffix-" + suffix;
        this.paoEndNumber = "paoEndNumber-" + suffix;
        this.paoEndSuffix = "paoEndSuffix-" + suffix;
        this.paoText = "paoText-" + suffix;
        this.saoText = "saoText-" + suffix;
        this.street = "street-" + suffix;
    }

    public Ordering build() {
        return new Ordering(
                saoStartNumber,
                saoStartSuffix,
                saoEndNumber,
                saoEndSuffix,
                paoStartNumber,
                paoStartSuffix,
                paoEndNumber,
                paoEndSuffix,
                paoText,
                saoText,
                street
        );
    }
}


