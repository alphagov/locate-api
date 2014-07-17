package uk.gov.gds.locate.api.services;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import uk.gov.gds.locate.api.model.Address;
import uk.gov.gds.locate.api.model.Presentation;
import uk.gov.gds.locate.api.model.SimpleAddress;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class AddressTransformationServiceTest {
    @Test
    public void shouldConvertAddressesToSimpleAddresses() {
        Presentation pOne = new Presentation("property-1", "street-1", "locality-1", "town-1", "area-1", "postcode-1");
        Presentation pTwo = new Presentation("property-2", "street-2", "locality-2", "town-2", "area-2", "postcode-2");
        Presentation pThree = new Presentation("property-3", "street-3", "locality-3", "town-3", "area-3", "postcode-3");

        Address aOne = new Address("gssCode-1", "uprn-1", pOne);
        Address aTwo = new Address("gssCode-2", "uprn-2", pTwo);
        Address aThree = new Address("gssCode-3", "uprn-3", pThree);

        List<SimpleAddress> transformed = AddressTransformationService.addressToSimpleAddress(ImmutableList.of(aOne, aTwo, aThree));

        assertThat(transformed.size()).isEqualTo(3);

        assertThat(transformed.get(0).getGssCode()).isEqualTo("gssCode-1");
        assertThat(transformed.get(0).getUprn()).isEqualTo("uprn-1");
        assertThat(transformed.get(0).getProperty()).isEqualTo("property-1");
        assertThat(transformed.get(0).getStreet()).isEqualTo("street-1");
        assertThat(transformed.get(0).getLocality()).isEqualTo("locality-1");
        assertThat(transformed.get(0).getTown()).isEqualTo("town-1");
        assertThat(transformed.get(0).getArea()).isEqualTo("area-1");

        assertThat(transformed.get(1).getGssCode()).isEqualTo("gssCode-2");
        assertThat(transformed.get(1).getUprn()).isEqualTo("uprn-2");
        assertThat(transformed.get(1).getProperty()).isEqualTo("property-2");
        assertThat(transformed.get(1).getStreet()).isEqualTo("street-2");
        assertThat(transformed.get(1).getLocality()).isEqualTo("locality-2");
        assertThat(transformed.get(1).getTown()).isEqualTo("town-2");
        assertThat(transformed.get(1).getArea()).isEqualTo("area-2");

        assertThat(transformed.get(2).getGssCode()).isEqualTo("gssCode-3");
        assertThat(transformed.get(2).getUprn()).isEqualTo("uprn-3");
        assertThat(transformed.get(2).getProperty()).isEqualTo("property-3");
        assertThat(transformed.get(2).getStreet()).isEqualTo("street-3");
        assertThat(transformed.get(2).getLocality()).isEqualTo("locality-3");
        assertThat(transformed.get(2).getTown()).isEqualTo("town-3");
        assertThat(transformed.get(2).getArea()).isEqualTo("area-3");
    }


}