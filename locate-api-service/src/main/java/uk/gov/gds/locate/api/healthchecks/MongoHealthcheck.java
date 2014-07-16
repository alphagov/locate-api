package uk.gov.gds.locate.api.healthchecks;

import com.mongodb.Mongo;
import com.yammer.metrics.core.HealthCheck;

public class MongoHealthcheck extends HealthCheck {

    private final Mongo mongo;

    public MongoHealthcheck(Mongo mongo) {
        super("MongoHealthcheck");
        this.mongo = mongo;
    }

    @Override
    protected Result check() throws Exception {
        mongo.getDatabaseNames().contains("locate");
        return Result.healthy();
    }
}
