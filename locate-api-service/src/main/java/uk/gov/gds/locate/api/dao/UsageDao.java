package uk.gov.gds.locate.api.dao;

import com.google.common.base.Optional;
import com.mongodb.BasicDBObject;
import com.yammer.metrics.annotation.Timed;
import org.mongojack.DBUpdate;
import org.mongojack.JacksonDBCollection;
import uk.gov.gds.locate.api.model.Usage;

public class UsageDao {

    private final JacksonDBCollection<Usage, String> collection;

    public UsageDao(JacksonDBCollection<Usage, String> collection) {
        this.collection = collection;
    }

    @Timed
    public Optional<Usage> findUsageByIdentifier(String identifier) {
        return Optional.fromNullable(collection.findAndModify(new BasicDBObject("identifier", identifier), new DBUpdate.Builder().inc("count")));
    }

    @Timed
    public Boolean create(String identifier) {
        return collection.insert(new Usage(org.bson.types.ObjectId.get().toString(), identifier, 1)).getN() == 1;
    }
}
