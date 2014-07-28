package uk.gov.gds.locate.api.helpers;

import uk.gov.gds.locate.api.model.Presentation;

public class PresentationBuilder {

    private String property;
    private String street;
    private String locality;
    private String town;
    private String area;
    private String postcode;

    public PresentationBuilder(String suffix) {
        this.property = "property-" + suffix;
        this.street = "street-" + suffix;
        this.locality = "locality-" + suffix;
        this.town = "town-" + suffix;
        this.area = "area-" + suffix;
        this.postcode = "postcode-" + suffix;
    }

    public PresentationBuilder property(String p) {
        this.property = p;
        return this;
    }

    public PresentationBuilder street(String p) {
        this.street = p;
        return this;
    }

    public PresentationBuilder locality(String p) {
        this.locality = p;
        return this;
    }

    public PresentationBuilder town(String p) {
        this.town = p;
        return this;
    }

    public PresentationBuilder area(String p) {
        this.area = p;
        return this;
    }

    public Presentation build() {
        return new Presentation(property, street, locality, town, area, postcode);
    }
}


