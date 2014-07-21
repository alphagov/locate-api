package uk.gov.gds.locate.api.services;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import uk.gov.gds.locate.api.model.Address;
import uk.gov.gds.locate.api.model.SimpleAddress;

import javax.annotation.Nullable;
import java.util.List;

public class AddressTransformationService {

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

    public static List<SimpleAddress> addressToSimpleAddress(List<Address> addresses) {
        return Lists.transform(addresses, new Function<Address, SimpleAddress>() {
            @Nullable
            @Override
            public SimpleAddress apply(@Nullable Address input) {
                return new SimpleAddress(input);
            }
        });
    }

}
