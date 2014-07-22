package uk.gov.gds.locate.api.dao;

import com.google.common.base.Optional;
import com.mongodb.BasicDBObject;
import org.mongojack.DBUpdate;
import org.mongojack.JacksonDBCollection;
import uk.gov.gds.locate.api.model.Usage;

public class UsageDao {

    private final JacksonDBCollection<Usage, String> collection;

    public UsageDao(JacksonDBCollection<Usage, String> collection) {
        this.collection = collection;
    }

    public Optional<Usage> findRateMeterById(String identifier) {
        return Optional.fromNullable(collection.findAndModify(new BasicDBObject("identifier", identifier), new DBUpdate.Builder().inc("count")));
    }

}
