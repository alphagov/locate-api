package uk.gov.gds.locate.api.helpers;

import uk.gov.gds.locate.api.model.Ordering;

public class OrderingBuilder {

    private Integer saoStartNumber;
    private String saoStartSuffix;
    private Integer saoEndNumber;
    private String saoEndSuffix;
    private Integer paoStartNumber;
    private String paoStartSuffix;
    private Integer paoEndNumber;
    private String paoEndSuffix;
    private String paoText;
    private String saoText;
    private String street;


    public OrderingBuilder() {
        this.saoStartNumber = null;
        this.saoStartSuffix = null;
        this.saoEndNumber = null;
        this.saoEndSuffix = null;
        this.paoStartNumber = null;
        this.paoStartSuffix = null;
        this.paoEndNumber = null;
        this.paoEndSuffix = null;
        this.paoText = null;
        this.saoText = null;
        this.street = null;
    }

    public OrderingBuilder saoStartNumber(Integer i) {
        this.saoStartNumber = i;
        return this;
    }

    public OrderingBuilder saoStartSuffix(String i) {
        this.saoStartSuffix = i;
        return this;
    }

    public OrderingBuilder saoEndNumber(Integer i) {
        this.saoEndNumber = i;
        return this;
    }

    public OrderingBuilder saoEndSuffix(String i) {
        this.saoEndSuffix = i;
        return this;
    }

    public OrderingBuilder paoStartNumber(Integer i) {
        this.paoStartNumber = i;
        return this;
    }

    public OrderingBuilder paoStartSuffix(String i) {
        this.paoStartSuffix = i;
        return this;
    }

    public OrderingBuilder paoEndNumber(Integer i) {
        this.paoEndNumber = i;
        return this;
    }

    public OrderingBuilder paoEndSuffix(String i) {
        this.paoEndSuffix = i;
        return this;
    }

    public OrderingBuilder paoText(String i) {
        this.paoText = i;
        return this;
    }

    public OrderingBuilder saoText(String i) {
        this.saoText = i;
        return this;
    }

    public OrderingBuilder street(String i) {
        this.street = i;
        return this;
    }

    public Ordering build() {
        return new Ordering(
                this.saoStartNumber,
                this.saoStartSuffix,
                this.saoEndNumber,
                this.saoEndSuffix,
                this.paoStartNumber,
                this.paoStartSuffix,
                this.paoEndNumber,
                this.paoEndSuffix,
                this.paoText,
                this.saoText,
                this.street
        );
    }
}


