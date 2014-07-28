package uk.gov.gds.locate.api.services;

import com.google.common.collect.ImmutableList;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import uk.gov.gds.locate.api.encryption.AesEncryptionProduct;
import uk.gov.gds.locate.api.helpers.DetailsBuilder;
import uk.gov.gds.locate.api.helpers.OrderingBuilder;
import uk.gov.gds.locate.api.helpers.PresentationBuilder;
import uk.gov.gds.locate.api.model.*;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

import static org.apache.commons.codec.binary.Base64.encodeBase64String;
import static org.fest.assertions.api.Assertions.assertThat;
import static uk.gov.gds.locate.api.encryption.AesEncryptionService.aesKeyFromBase64EncodedString;
import static uk.gov.gds.locate.api.encryption.AesEncryptionService.encrypt;
import static uk.gov.gds.locate.api.services.AddressTransformationService.*;

public class AddressTransformationServiceTest {
    private Ordering o = new OrderingBuilder().build();
    private final static SecureRandom random = new SecureRandom();
    private String testKey = "QWVzS2B5QmVpbmdTb21lU3RyaW5nT2ZMZW5ndGgyNTY=";

    private static KeyGenerator keyGenerator = null;

    static {
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Failed to instantiate key generator");
        }
    }

    @Test
    public void shouldConvertAddressToSimpleAddressSettingFieldsToNullIfNotPresent() {
        Details d = new DetailsBuilder("1").commercial(true).postal(true).residential(true).electoral(true).higherEducational(true).build();
        Presentation p = new Presentation(null, null, null, null, null, null);
        Address address = new Address("gssCode-1", "uprn-1", "postcode-1", "country", new Date(), p, d, location(1.1, 2.2), new OrderingBuilder().build(), "iv");

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

        Ordering o1 = new OrderingBuilder().build();
        Ordering o2 = new OrderingBuilder().build();
        Ordering o3 = new OrderingBuilder().build();

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


        Address aPostal = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), isNotPostal, location(1.1, 2.2), o, "iv");
        Address aNotPostal = new Address("gssCode-2", "uprn-2", "postcode-2", "country-2", new Date(), new PresentationBuilder("postal").build(), isPostal, location(1.1, 2.2), o, "iv");
        Address aNotResidential = new Address("gssCode-2", "uprn-2", "postcode-3", "country-3", new Date(), new PresentationBuilder("not-residential").build(), isNotResidential, location(1.1, 2.2), o, "iv");

        List<Address> transformed = applyPredicate(ImmutableList.of(aPostal, aNotPostal, aNotResidential), QueryType.RESIDENTIAL.predicate());

        assertThat(transformed.size()).isEqualTo(1);
        assertThat(transformed.get(0).getPresentation().getProperty()).isEqualTo("property-postal");
    }

    @Test
    public void shouldIncludeOnlyPostalCommercialAddress() {
        Details isNotPostal = new DetailsBuilder("not-postal").postal(false).commercial(true).build();
        Details isPostal = new DetailsBuilder("postal").postal(true).commercial(true).build();
        Details isNotCommercial = new DetailsBuilder("not-residential").postal(true).residential(true).commercial(false).build();

        Address aPostal = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), isNotPostal, location(1.1, 2.2), o, "iv");
        Address aNotPostal = new Address("gssCode-2", "uprn-2", "postcode-2", "country-2", new Date(), new PresentationBuilder("postal").build(), isPostal, location(1.1, 2.2), o, "iv");
        Address aNotCommercial = new Address("gssCode-2", "uprn-2", "postcode-3", "country-3", new Date(), new PresentationBuilder("not-residential").build(), isNotCommercial, location(1.1, 2.2), o, "iv");

        List<Address> transformed = applyPredicate(ImmutableList.of(aPostal, aNotPostal, aNotCommercial), QueryType.COMMERCIAL.predicate());

        assertThat(transformed.size()).isEqualTo(1);
        assertThat(transformed.get(0).getPresentation().getProperty()).isEqualTo("property-postal");
    }

    @Test
    public void shouldIncludeOnlyPostalElectoralAddress() {
        Details isNotPostal = new DetailsBuilder("not-postal").postal(false).electoral(true).build();
        Details isPostal = new DetailsBuilder("postal").postal(true).electoral(true).build();
        Details isNotElectoral = new DetailsBuilder("postal").postal(true).electoral(false).residential(true).build();

        Address aPostal = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), isNotPostal, location(1.1, 2.2), o, "iv");
        Address aNotPostal = new Address("gssCode-2", "uprn-2", "postcode-1", "country-1", new Date(), new PresentationBuilder("postal").build(), isPostal, location(1.1, 2.2), o, "iv");
        Address aNotElectoral = new Address("gssCode-2", "uprn-2", "postcode-1", "country-1", new Date(), new PresentationBuilder("postal").build(), isNotElectoral, location(1.1, 2.2), o, "iv");


        List<Address> transformed = applyPredicate(ImmutableList.of(aPostal, aNotPostal, aNotElectoral), QueryType.ELECTORAL.predicate());

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

        Address one = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), isNotPostal1, location(1.1, 2.2), o, "iv");
        Address two = new Address("gssCode-2", "uprn-2", "postcode-2", "country-2", new Date(), new PresentationBuilder("not-postal").build(), isNotPostal2, location(1.1, 2.2), o, "iv");
        Address three = new Address("gssCode-3", "uprn-3", "postcode-3", "country-3", new Date(), new PresentationBuilder("postal-3").build(), isPostal1, location(1.1, 2.2), o, "iv");
        Address four = new Address("gssCode-4", "uprn-4", "postcode-4", "country-4", new Date(), new PresentationBuilder("postal-4").build(), isPostal2, location(1.1, 2.2), o, "iv");
        Address notCommercialOrResidential = new Address("gssCode-5", "uprn-5", "postcode-5", "country-5", new Date(), new PresentationBuilder("postal-2").build(), isNotCommercialOrResidential, location(1.1, 2.2), o, "iv");

        List<Address> transformed = applyPredicate(ImmutableList.of(one, two, three, four, notCommercialOrResidential), QueryType.RESIDENTIAL_AND_COMMERCIAL.predicate());

        assertThat(transformed.size()).isEqualTo(2);
        assertThat(transformed.get(0).getPresentation().getProperty()).isEqualTo("property-postal-3");
        assertThat(transformed.get(1).getPresentation().getProperty()).isEqualTo("property-postal-4");
    }

    @Test
    public void shouldLeaveOrderAsIsIfAllFieldsAreNull() {
        Details d = new DetailsBuilder("postal").postal(true).residential(true).build();

        Ordering o1 = new OrderingBuilder().build();
        Ordering o2 = new OrderingBuilder().build();

        Address first = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o1, "iv");
        Address second = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o2, "iv");

        List<Address> sorted = orderAddresses(ImmutableList.of(second, first));
        assertThat(sorted.get(1)).isEqualTo(first);
        assertThat(sorted.get(0)).isEqualTo(second);
    }

    @Test
    public void shouldOrderAddressesBySaoNumber() {
        Details d = new DetailsBuilder("postal").postal(true).residential(true).build();

        Ordering o1 = new OrderingBuilder().saoStartNumber(1).build();
        Ordering o2 = new OrderingBuilder().saoStartNumber(2).build();

        Address first = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o1, "iv");
        Address second = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o2, "iv");

        List<Address> sorted = orderAddresses(ImmutableList.of(second, first));
        assertThat(sorted.get(0)).isEqualTo(first);
        assertThat(sorted.get(1)).isEqualTo(second);
    }

    @Test
    public void shouldOrderAddressesBySaoNumberAndSuffix() {
        Details d = new DetailsBuilder("postal").postal(true).residential(true).build();

        Ordering o1 = new OrderingBuilder().saoStartNumber(1).saoStartSuffix("a").build();
        Ordering o2 = new OrderingBuilder().saoStartNumber(1).saoStartSuffix("b").build();

        Address first = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o1, "iv");
        Address second = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o2, "iv");

        List<Address> sorted = orderAddresses(ImmutableList.of(second, first));
        assertThat(sorted.get(0)).isEqualTo(first);
        assertThat(sorted.get(1)).isEqualTo(second);
    }

    @Test
    public void shouldOrderAddressesByPaoNumberAndSuffix() {
        Details d = new DetailsBuilder("postal").postal(true).residential(true).build();

        Ordering o1 = new OrderingBuilder().saoStartNumber(1).saoStartSuffix("a").paoStartNumber(100).paoStartSuffix("a").build();
        Ordering o2 = new OrderingBuilder().saoStartNumber(1).saoStartSuffix("a").paoStartNumber(100).paoStartSuffix("b").build();

        Address first = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o1, "iv");
        Address second = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o2, "iv");

        List<Address> sorted = orderAddresses(ImmutableList.of(second, first));
        assertThat(sorted.get(0)).isEqualTo(first);
        assertThat(sorted.get(1)).isEqualTo(second);
    }

    @Test
    public void shouldOrderAddressesBySaoText() {
        Details d = new DetailsBuilder("postal").postal(true).residential(true).build();

        Ordering o1 = new OrderingBuilder()
                .saoStartNumber(1).saoStartSuffix("a").saoEndNumber(1).saoEndSuffix("a")
                .paoStartNumber(1).paoStartSuffix("a").paoEndNumber(1).paoEndSuffix("a")
                .saoText("a")
                .build();
        Ordering o2 = new OrderingBuilder()
                .saoStartNumber(1).saoStartSuffix("a").saoEndNumber(1).saoEndSuffix("a")
                .paoStartNumber(1).paoStartSuffix("a").paoEndNumber(1).paoEndSuffix("a")
                .saoText("b")
                .build();

        Address first = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o1, "iv");
        Address second = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o2, "iv");

        List<Address> sorted = orderAddresses(ImmutableList.of(second, first));
        assertThat(sorted.get(0)).isEqualTo(first);
        assertThat(sorted.get(1)).isEqualTo(second);
    }

    @Test
    public void shouldOrderAddressesByPaoText() {
        Details d = new DetailsBuilder("postal").postal(true).residential(true).build();

        Ordering o1 = new OrderingBuilder()
                .saoStartNumber(1).saoStartSuffix("a").saoEndNumber(1).saoEndSuffix("a")
                .paoStartNumber(1).paoStartSuffix("a").paoEndNumber(1).paoEndSuffix("a")
                .saoText("a")
                .paoText("a")
                .build();
        Ordering o2 = new OrderingBuilder()
                .saoStartNumber(1).saoStartSuffix("a").saoEndNumber(1).saoEndSuffix("a")
                .paoStartNumber(1).paoStartSuffix("a").paoEndNumber(1).paoEndSuffix("a")
                .saoText("a")
                .paoText("b")
                .build();

        Address first = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o1, "iv");
        Address second = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o2, "iv");

        List<Address> sorted = orderAddresses(ImmutableList.of(second, first));
        assertThat(sorted.get(0)).isEqualTo(first);
        assertThat(sorted.get(1)).isEqualTo(second);
    }

    @Test
    public void shouldOrderAddressesByStreet() {
        Details d = new DetailsBuilder("postal").postal(true).residential(true).build();

        Ordering o1 = new OrderingBuilder()
                .saoStartNumber(1).saoStartSuffix("a").saoEndNumber(1).saoEndSuffix("a")
                .paoStartNumber(1).paoStartSuffix("a").paoEndNumber(1).paoEndSuffix("a")
                .saoText("a")
                .paoText("a")
                .street("a")
                .build();
        Ordering o2 = new OrderingBuilder()
                .saoStartNumber(1).saoStartSuffix("a").saoEndNumber(1).saoEndSuffix("a")
                .paoStartNumber(1).paoStartSuffix("a").paoEndNumber(1).paoEndSuffix("a")
                .saoText("a")
                .paoText("a")
                .street("b")
                .build();

        Address first = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o1, "iv");
        Address second = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o2, "iv");

        List<Address> sorted = orderAddresses(ImmutableList.of(second, first));
        assertThat(sorted.get(0)).isEqualTo(first);
        assertThat(sorted.get(1)).isEqualTo(second);
    }

    @Test
    public void shouldLeaveIdenticalAddressesInOrderReceievd() {
        Details d = new DetailsBuilder("postal").postal(true).residential(true).build();

        Ordering o1 = new OrderingBuilder()
                .saoStartNumber(1).saoStartSuffix("a").saoEndNumber(1).saoEndSuffix("a")
                .paoStartNumber(1).paoStartSuffix("a").paoEndNumber(1).paoEndSuffix("a")
                .saoText("a")
                .paoText("a")
                .street("a")
                .build();
        Ordering o2 = new OrderingBuilder()
                .saoStartNumber(1).saoStartSuffix("a").saoEndNumber(1).saoEndSuffix("a")
                .paoStartNumber(1).paoStartSuffix("a").paoEndNumber(1).paoEndSuffix("a")
                .saoText("a")
                .paoText("a")
                .street("a")
                .build();

        Address first = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o1, "iv");
        Address second = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), new PresentationBuilder("not-postal").build(), d, location(1.1, 2.2), o2, "iv");

        List<Address> sorted = orderAddresses(ImmutableList.of(second, first));
        assertThat(sorted.get(1)).isEqualTo(first);
        assertThat(sorted.get(0)).isEqualTo(second);
    }

    @Test
    public void shouldDecryptEncryptedFieldsOnAddressLeavingUnencryptedFieldsAlone() throws Exception {
        Details d = new DetailsBuilder("postal").postal(true).residential(true).build();

        AesEncryptionProduct encryptedBit = encrypt("this this this", aesKeyFromBase64EncodedString(testKey));
        Presentation p = new PresentationBuilder("test")
                .property(encodeBase64String(encryptedBit.getEncryptedContent()))
                .street(encodeBase64String(encryptedBit.getEncryptedContent()))
                .locality(encodeBase64String(encryptedBit.getEncryptedContent()))
                .town(encodeBase64String(encryptedBit.getEncryptedContent()))
                .area(encodeBase64String(encryptedBit.getEncryptedContent()))
                .build();

        Ordering o = new OrderingBuilder()
                .saoText(encodeBase64String(encryptedBit.getEncryptedContent()))
                .paoText(encodeBase64String(encryptedBit.getEncryptedContent()))
                .street(encodeBase64String(encryptedBit.getEncryptedContent()))
                .build();

        Address a = new Address("gssCode-1", "uprn-1", "postcode-1", "country-1", new Date(), p, d, location(1.1, 2.2), o, Base64.encodeBase64String(encryptedBit.getInitializationVector()));

        List<Address> decrypted = decryptAddresses(ImmutableList.of(a), testKey);
        assertThat(decrypted.get(0).getPresentation().getProperty()).isEqualTo("this this this");
    }


    private Location location(Double latitude, Double longitude) {
        return new Location(latitude, longitude);
    }


}