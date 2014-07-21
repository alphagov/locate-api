package uk.gov.gds.locate.api.services;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import uk.gov.gds.locate.api.model.*;

import java.util.Date;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class AddressTransformationServiceTest {

    @Test
    public void shouldConvertAddressToSimpleAddressSettingFieldsToNullIfNotPresent() {
        Details d = new DetailsBuilder("1").commercial(true).postal(true).residential(true).electoral(true).higherEducational(true).build();
        Presentation p = new Presentation(null, null, null, null, null, null);
        Address address = new Address("gssCode-1", "uprn-1", p, d, location(1.1, 2.2));

        List<SimpleAddress> transformed = AddressTransformationService.addressToSimpleAddress(ImmutableList.of(address));

        assertThat(transformed.size()).isEqualTo(1);

        assertThat(transformed.get(0).getGssCode()).isEqualTo("gssCode-1");
        assertThat(transformed.get(0).getUprn()).isEqualTo("uprn-1");
        assertThat(transformed.get(0).getProperty()).isNullOrEmpty();
        assertThat(transformed.get(0).getStreet()).isNullOrEmpty();
        assertThat(transformed.get(0).getLocality()).isNullOrEmpty();
        assertThat(transformed.get(0).getTown()).isNullOrEmpty();
        assertThat(transformed.get(0).getArea()).isNullOrEmpty();
    }

    @Test
    public void shouldConvertAddressesToSimpleAddresses() {

        Details detailsOne = new DetailsBuilder("1").commercial(true).postal(true).residential(true).electoral(true).higherEducational(true).build();
        Details detailsTwo = new DetailsBuilder("2").commercial(true).postal(true).residential(true).electoral(true).higherEducational(true).build();
        Details detailsThree = new DetailsBuilder("3").commercial(true).postal(true).residential(true).electoral(true).higherEducational(true).build();

        Presentation pOne = new PresentationBuilder("1").build();
        Presentation pTwo = new PresentationBuilder("2").build();
        Presentation pThree = new PresentationBuilder("3").build();

        Address aOne = new Address("gssCode-1", "uprn-1", pOne, detailsOne, location(1.1, 2.2));
        Address aTwo = new Address("gssCode-2", "uprn-2", pTwo, detailsTwo, location(1.1, 2.2));
        Address aThree = new Address("gssCode-3", "uprn-3", pThree, detailsThree, location(1.1, 2.2));

        List<SimpleAddress> transformed = AddressTransformationService.addressToSimpleAddress(ImmutableList.of(aOne, aTwo, aThree));

        assertThat(transformed.size()).isEqualTo(3);

        assertThat(transformed.get(0).getGssCode()).isEqualTo("gssCode-1");
        assertThat(transformed.get(0).getUprn()).isEqualTo("uprn-1");
        assertThat(transformed.get(0).getProperty()).isEqualTo("property-1");
        assertThat(transformed.get(0).getStreet()).isEqualTo("street-1");
        assertThat(transformed.get(0).getLocality()).isEqualTo("locality-1");
        assertThat(transformed.get(0).getTown()).isEqualTo("town-1");
        assertThat(transformed.get(0).getArea()).isEqualTo("area-1");
        assertThat(transformed.get(0).getPostcode()).isEqualTo("postcode-1");

        assertThat(transformed.get(1).getGssCode()).isEqualTo("gssCode-2");
        assertThat(transformed.get(1).getUprn()).isEqualTo("uprn-2");
        assertThat(transformed.get(1).getProperty()).isEqualTo("property-2");
        assertThat(transformed.get(1).getStreet()).isEqualTo("street-2");
        assertThat(transformed.get(1).getLocality()).isEqualTo("locality-2");
        assertThat(transformed.get(1).getTown()).isEqualTo("town-2");
        assertThat(transformed.get(1).getArea()).isEqualTo("area-2");
        assertThat(transformed.get(1).getPostcode()).isEqualTo("postcode-2");

        assertThat(transformed.get(2).getGssCode()).isEqualTo("gssCode-3");
        assertThat(transformed.get(2).getUprn()).isEqualTo("uprn-3");
        assertThat(transformed.get(2).getProperty()).isEqualTo("property-3");
        assertThat(transformed.get(2).getStreet()).isEqualTo("street-3");
        assertThat(transformed.get(2).getLocality()).isEqualTo("locality-3");
        assertThat(transformed.get(2).getTown()).isEqualTo("town-3");
        assertThat(transformed.get(2).getArea()).isEqualTo("area-3");
        assertThat(transformed.get(2).getPostcode()).isEqualTo("postcode-3");
    }

    @Test
    public void shouldIncludeOnlyPostalResidentialAddress() {
        Details isNotPostal = new DetailsBuilder("not-postal").postal(false).residential(true).build();
        Details isPostal = new DetailsBuilder("postal").postal(true).residential(true).build();
        Details isNotResidential = new DetailsBuilder("not-residential").postal(true).residential(false).commercial(true).build();

        Address aPostal = new Address("gssCode-1", "uprn-1", new PresentationBuilder("not-postal").build(), isNotPostal, location(1.1, 2.2));
        Address aNotPostal = new Address("gssCode-2", "uprn-2", new PresentationBuilder("postal").build(), isPostal, location(1.1, 2.2));
        Address aNotResidential = new Address("gssCode-2", "uprn-2", new PresentationBuilder("not-residential").build(), isNotResidential, location(1.1, 2.2));

        List<Address> transformed = AddressTransformationService.filterForResidential(ImmutableList.of(aPostal, aNotPostal, aNotResidential));

        assertThat(transformed.size()).isEqualTo(1);
        assertThat(transformed.get(0).getPresentation().getProperty()).isEqualTo("property-postal");
    }

    @Test
    public void shouldIncludeOnlyPostalCommercialAddress() {
        Details isNotPostal = new DetailsBuilder("not-postal").postal(false).commercial(true).build();
        Details isPostal = new DetailsBuilder("postal").postal(true).commercial(true).build();
        Details isNotCommercial = new DetailsBuilder("not-residential").postal(true).residential(true).commercial(false).build();

        Address aPostal = new Address("gssCode-1", "uprn-1", new PresentationBuilder("not-postal").build(), isNotPostal, location(1.1, 2.2));
        Address aNotPostal = new Address("gssCode-2", "uprn-2", new PresentationBuilder("postal").build(), isPostal, location(1.1, 2.2));
        Address aNotCommercial = new Address("gssCode-2", "uprn-2", new PresentationBuilder("not-residential").build(), isNotCommercial, location(1.1, 2.2));

        List<Address> transformed = AddressTransformationService.filterForCommercial(ImmutableList.of(aPostal, aNotPostal, aNotCommercial));

        assertThat(transformed.size()).isEqualTo(1);
        assertThat(transformed.get(0).getPresentation().getProperty()).isEqualTo("property-postal");
    }

    @Test
    public void shouldIncludeOnlyPostalElectoralAddress() {
        Details isNotPostal = new DetailsBuilder("not-postal").postal(false).electoral(true).build();
        Details isPostal = new DetailsBuilder("postal").postal(true).electoral(true).build();
        Details isNotElectoral = new DetailsBuilder("postal").postal(true).electoral(false).residential(true).build();

        Address aPostal = new Address("gssCode-1", "uprn-1", new PresentationBuilder("not-postal").build(), isNotPostal, location(1.1, 2.2));
        Address aNotPostal = new Address("gssCode-2", "uprn-2", new PresentationBuilder("postal").build(), isPostal, location(1.1, 2.2));
        Address aNotElectoral = new Address("gssCode-2", "uprn-2", new PresentationBuilder("postal").build(), isNotElectoral, location(1.1, 2.2));


        List<Address> transformed = AddressTransformationService.filterForElectoral(ImmutableList.of(aPostal, aNotPostal, aNotElectoral));

        assertThat(transformed.size()).isEqualTo(1);
        assertThat(transformed.get(0).getPresentation().getProperty()).isEqualTo("property-postal");
    }

    @Test
    public void shouldIncludeOnlyPostalResidentialAndCommercialAddress() {
        Details isNotPostal1 = new DetailsBuilder("not-postal").postal(false).residential(true).build();
        Details isNotPostal2 = new DetailsBuilder("not-postal").postal(false).commercial(true).build();
        Details isPostal1 = new DetailsBuilder("postal").postal(true).residential(true).build();
        Details isPostal2 = new DetailsBuilder("postal").postal(true).commercial(true).build();
        Details isNotCommercialOrResidential = new DetailsBuilder("not-postal").postal(true).commercial(false).residential(false).higherEducational(true).build();

        Address one = new Address("gssCode-1", "uprn-1", new PresentationBuilder("not-postal").build(), isNotPostal1, location(1.1, 2.2));
        Address two = new Address("gssCode-2", "uprn-1", new PresentationBuilder("not-postal").build(), isNotPostal2, location(1.1, 2.2));
        Address three = new Address("gssCode-1", "uprn-1", new PresentationBuilder("postal-1").build(), isPostal1, location(1.1, 2.2));
        Address four = new Address("gssCode-2", "uprn-2", new PresentationBuilder("postal-2").build(), isPostal2, location(1.1, 2.2));
        Address notCommercialOrResidential = new Address("gssCode-2", "uprn-2", new PresentationBuilder("postal-2").build(), isNotCommercialOrResidential, location(1.1, 2.2));

        List<Address> transformed = AddressTransformationService.filterForResidentialAndCommercial(ImmutableList.of(one,two, three, four, notCommercialOrResidential));

        assertThat(transformed.size()).isEqualTo(2);
        assertThat(transformed.get(0).getPresentation().getProperty()).isEqualTo("property-postal-1");
        assertThat(transformed.get(1).getPresentation().getProperty()).isEqualTo("property-postal-2");
    }

    private Location location(Double latitude, Double longitude) {
        return new Location(latitude, longitude);
    }

    private class DetailsBuilder {
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

    private class PresentationBuilder {

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

        public Presentation build() {
            return new Presentation(property, street, locality, town, area, postcode);
        }
    }


}