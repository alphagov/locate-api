package uk.gov.gds.locate.api.services;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gds.locate.api.model.Address;
import uk.gov.gds.locate.api.model.SimpleAddress;

import javax.annotation.Nullable;
import java.util.List;

public class AddressTransformationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressTransformationService.class);


    public static List<Address> filterForElectoral(List<Address> addresses) {
        return ImmutableList.copyOf(Collections2.filter(addresses, new Predicate<Address>() {
            @Override
            public boolean apply(@Nullable Address input) {
                return input.getDetails().getIsElectoral() && input.getDetails().getIsPostalAddress();
            }
        }));
    }

    public static List<Address> filterForResidential(List<Address> addresses) {
        return ImmutableList.copyOf(Collections2.filter(addresses, new Predicate<Address>() {
            @Override
            public boolean apply(@Nullable Address input) {
                return input.getDetails().getIsResidential() && input.getDetails().getIsPostalAddress();
            }
        }));
    }

    public static List<Address> filterForCommercial(List<Address> addresses) {
        return ImmutableList.copyOf(Collections2.filter(addresses, new Predicate<Address>() {
            @Override
            public boolean apply(@Nullable Address input) {
                return input.getDetails().getIsCommercial() && input.getDetails().getIsPostalAddress();
            }
        }));
    }

    public static List<Address> filterForResidentialAndCommercial(List<Address> addresses) {
        return ImmutableList.copyOf(Collections2.filter(addresses, new Predicate<Address>() {
            @Override
            public boolean apply(@Nullable Address input) {
                return (input.getDetails().getIsCommercial() || input.getDetails().getIsResidential()) && input.getDetails().getIsPostalAddress();
            }
        }));
    }

    public static List<Address> filter(List<Address> addresses, Predicate<Address> predicate) {
        return ImmutableList.copyOf(Collections2.filter(addresses, predicate));
    }

    public static List<SimpleAddress> addressToSimpleAddress(List<Address> addresses) {
        return Lists.transform(addresses, new Function<Address, SimpleAddress>() {
            @Nullable
            @Override
            public SimpleAddress apply(@Nullable Address input) {
                return new SimpleAddress(input);
            }
        });
    }

    public static List<Address> decryptAndOrderAddress(List<Address> addresses, final String key) {
        return Lists.transform(Ordering.natural().sortedCopy(addresses), new Function<Address, Address>() {
            @Nullable
            @Override
            public Address apply(@Nullable Address input) {
                LOGGER.info("" + input);
                Address a=  input.decrypt(key, input.getIv());
                LOGGER.info("" + a);
                return a;
            }
        });
    }


}
