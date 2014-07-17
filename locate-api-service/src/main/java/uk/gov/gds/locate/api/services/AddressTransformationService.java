package uk.gov.gds.locate.api.services;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import uk.gov.gds.locate.api.model.Address;
import uk.gov.gds.locate.api.model.SimpleAddress;

import javax.annotation.Nullable;
import java.util.List;

public class AddressTransformationService {


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
