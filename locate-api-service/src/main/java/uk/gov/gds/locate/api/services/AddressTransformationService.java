package uk.gov.gds.locate.api.services;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import uk.gov.gds.locate.api.model.Address;
import uk.gov.gds.locate.api.model.SimpleAddress;

import javax.annotation.Nullable;
import java.util.List;

public class AddressTransformationService {

    public static List<Address> applyPredicate(List<Address> addresses, Predicate<Address> predicate) {
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

    public static List<Address> decryptAddresses(List<Address> addresses, final String key) {
        return Lists.transform(addresses, new Function<Address, Address>() {
            @Nullable
            @Override
            public Address apply(@Nullable Address input) {
                return input.decrypt(key, input.getIv());
            }
        });
    }

    public static List<Address> orderAddresses(List<Address> addresses) {
        return Ordering.natural().sortedCopy(addresses);
    }
}
