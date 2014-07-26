package uk.gov.gds.locate.api.services;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import uk.gov.gds.locate.api.helpers.DetailsBuilder;
import uk.gov.gds.locate.api.helpers.OrderingBuilder;
import uk.gov.gds.locate.api.helpers.PresentationBuilder;
import uk.gov.gds.locate.api.model.*;

import java.util.Date;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class AddressTransformationServiceTest {
    private Ordering o = new OrderingBuilder("1").build();

    @Test
    public void shouldConvertAddressToSimpleAddressSettingFieldsToNullIfNotPresent() {
        Details d = new DetailsBuilder("1").commercial(true).postal(true).residential(true).electoral(true).higherEducational(true).build();
        Presentation p = new Presentation(null, null, null, null, null, null);
        Address address = new Address("gssCode-1", "uprn-1", "postcode-1", "country", new Date(), p, d, location(1.1, 2.2), new OrderingBuilder("test").build(), "iv");

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

        Ordering o1 = new OrderingBuilder("1").build();
        Ordering o2 = new OrderingBuilder("2").build();
        Ordering o3 = new OrderingBuilder("3").build();

        Address aOne = new Address("gssCode-1", "uprn-1", "postcode-1", "country", new Date(), pOne, detailsOne, location(1.1, 2.2), o1, "iv");
        Address aTwo = new Address("gssCode-2", "uprn-2", "postcode-2", "country-2", new Date(), pTwo, detailsTwo, location(1.1, 2.2), o2, "iv");
        Address aThree = new Address("gssCode-3", "uprn-3", "postcode-3", "country-3", new Date(), pThree, detailsThree, location(1.1, 2.2), o3, "iv");

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


        Address aPostal = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), isNotPostal, location(1.1, 2.2),o , "iv");
        Address aNotPostal = new Address("gssCode-2", "uprn-2", "postcode-2", "country-2", new Date(), new PresentationBuilder("postal").build(), isPostal, location(1.1, 2.2),o , "iv");
        Address aNotResidential = new Address("gssCode-2", "uprn-2", "postcode-3", "country-3", new Date(), new PresentationBuilder("not-residential").build(), isNotResidential, location(1.1, 2.2),o , "iv");

        List<Address> transformed = AddressTransformationService.filterForResidential(ImmutableList.of(aPostal, aNotPostal, aNotResidential));

        assertThat(transformed.size()).isEqualTo(1);
        assertThat(transformed.get(0).getPresentation().getProperty()).isEqualTo("property-postal");
    }

    @Test
    public void shouldIncludeOnlyPostalCommercialAddress() {
        Details isNotPostal = new DetailsBuilder("not-postal").postal(false).commercial(true).build();
        Details isPostal = new DetailsBuilder("postal").postal(true).commercial(true).build();
        Details isNotCommercial = new DetailsBuilder("not-residential").postal(true).residential(true).commercial(false).build();

        Address aPostal = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(),new PresentationBuilder("not-postal").build(), isNotPostal, location(1.1, 2.2),o , "iv");
        Address aNotPostal = new Address("gssCode-2", "uprn-2", "postcode-2", "country-2", new Date(),new PresentationBuilder("postal").build(), isPostal, location(1.1, 2.2),o , "iv");
        Address aNotCommercial = new Address("gssCode-2", "uprn-2", "postcode-3", "country-3", new Date(),new PresentationBuilder("not-residential").build(), isNotCommercial, location(1.1, 2.2),o , "iv");

        List<Address> transformed = AddressTransformationService.filterForCommercial(ImmutableList.of(aPostal, aNotPostal, aNotCommercial));

        assertThat(transformed.size()).isEqualTo(1);
        assertThat(transformed.get(0).getPresentation().getProperty()).isEqualTo("property-postal");
    }

    @Test
    public void shouldIncludeOnlyPostalElectoralAddress() {
        Details isNotPostal = new DetailsBuilder("not-postal").postal(false).electoral(true).build();
        Details isPostal = new DetailsBuilder("postal").postal(true).electoral(true).build();
        Details isNotElectoral = new DetailsBuilder("postal").postal(true).electoral(false).residential(true).build();

        Address aPostal = new Address("gssCode-1", "uprn-1","postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), isNotPostal, location(1.1, 2.2),o , "iv");
        Address aNotPostal = new Address("gssCode-2", "uprn-2","postcode-1", "country-1", new Date(), new PresentationBuilder("postal").build(), isPostal, location(1.1, 2.2),o , "iv");
        Address aNotElectoral = new Address("gssCode-2", "uprn-2", "postcode-1", "country-1", new Date(),new PresentationBuilder("postal").build(), isNotElectoral, location(1.1, 2.2),o , "iv");


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

        Address one = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(),new PresentationBuilder("not-postal").build(), isNotPostal1, location(1.1, 2.2), o, "iv");
        Address two = new Address("gssCode-2", "uprn-2","postcode-2", "country-2", new Date(), new PresentationBuilder("not-postal").build(), isNotPostal2, location(1.1, 2.2), o, "iv");
        Address three = new Address("gssCode-3", "uprn-3", "postcode-3", "country-3", new Date(),new PresentationBuilder("postal-3").build(), isPostal1, location(1.1, 2.2), o, "iv");
        Address four = new Address("gssCode-4", "uprn-4", "postcode-4", "country-4", new Date(),new PresentationBuilder("postal-4").build(), isPostal2, location(1.1, 2.2), o, "iv");
        Address notCommercialOrResidential = new Address("gssCode-5", "uprn-5", "postcode-5", "country-5", new Date(),new PresentationBuilder("postal-2").build(), isNotCommercialOrResidential, location(1.1, 2.2), o, "iv");

        List<Address> transformed = AddressTransformationService.filterForResidentialAndCommercial(ImmutableList.of(one, two, three, four, notCommercialOrResidential));

        assertThat(transformed.size()).isEqualTo(2);
        assertThat(transformed.get(0).getPresentation().getProperty()).isEqualTo("property-postal-3");
        assertThat(transformed.get(1).getPresentation().getProperty()).isEqualTo("property-postal-4");
    }

    private Location location(Double latitude, Double longitude) {
        return new Location(latitude, longitude);
    }


}