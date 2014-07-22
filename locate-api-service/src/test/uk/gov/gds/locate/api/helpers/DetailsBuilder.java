package uk.gov.gds.locate.api.helpers;

import uk.gov.gds.locate.api.model.Details;

import java.util.Date;

public class DetailsBuilder {
    private Date blpuCreatedAt;
    private Date blpuUpdatedAt;
    private String classification;
    private String state;
    private Boolean isPostalAddress;
    private Boolean isCommercial;
    private Boolean isResidential;
    private Boolean isHigherEducational;
    private Boolean isElectoral;
    private String usrn;
    private String file;
    private String primaryClassification;
    private String secondaryClassification;

    public DetailsBuilder(String suffix) {
        isPostalAddress = false;
        isCommercial = false;
        isResidential = false;
        isHigherEducational = false;
        isElectoral = false;
        blpuCreatedAt = new Date();
        blpuUpdatedAt = new Date();
        classification = "classification-" + suffix;
        state = "state-" + suffix;
        usrn = "usrn-" + suffix;
        file = "file-" + suffix;
        primaryClassification = "primaryClassification-" + suffix;
        secondaryClassification = "secondaryClassification-" + suffix;
    }

    public DetailsBuilder postal(Boolean isPostalAddress) {
        this.isPostalAddress = isPostalAddress;
        return this;
    }

    public DetailsBuilder residential(Boolean isResidential) {
        this.isResidential = isResidential;
        return this;
    }

    public DetailsBuilder commercial(Boolean isCommercial) {
        this.isCommercial = isCommercial;
        return this;
    }

    public DetailsBuilder electoral(Boolean isElectoral) {
        this.isElectoral = isElectoral;
        return this;
    }

    public DetailsBuilder higherEducational(Boolean isHigherEducational) {
        this.isHigherEducational = isHigherEducational;
        return this;
    }

    public Details build() {
        return new Details(blpuCreatedAt, blpuUpdatedAt, classification, state, isPostalAddress, isCommercial, isResidential, isHigherEducational, isElectoral, usrn, file, primaryClassification, secondaryClassification);
    }
}