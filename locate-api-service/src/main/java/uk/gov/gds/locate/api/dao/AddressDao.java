package uk.gov.gds.locate.api.dao;


import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;
import uk.gov.gds.locate.api.model.Address;

import java.util.List;

public class AddressDao {

    private final JacksonDBCollection<Address, String> addresses;

    public AddressDao(JacksonDBCollection<Address, String> addresses) {
        this.addresses = addresses;
    }

    public List<Address> findAllForPostcode(String postcode) {
        DBCursor<Address> found = addresses.find().is("postcode", postcode);
        return found.toArray();
    }
}
