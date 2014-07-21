package uk.gov.gds.locate.api.services;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import uk.gov.gds.locate.api.model.Address;
import uk.gov.gds.locate.api.model.SimpleAddress;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

public class AddressTransformationService {

    public static List<Address> filterResidential(List<Address> addresses) {
        return ImmutableList.copyOf(Collections2.filter(addresses, new Predicate<Address>() {
            @Override
            public boolean apply(@Nullable Address input) {
                return input.getDetails().getIsResidential() && input.getDetails().getIsPostalAddress();
            }
        }));
    }

    public static List<Address> filterCommercial(List<Address> addresses) {
        return ImmutableList.copyOf(Collections2.filter(addresses, new Predicate<Address>() {
            @Override
            public boolean apply(@Nullable Address input) {
                return input.getDetails().getIsCommercial() && input.getDetails().getIsPostalAddress();
            }
        }));
    }

    public static List<Address> filterResidentialAndCommercial(List<Address> addresses) {
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
