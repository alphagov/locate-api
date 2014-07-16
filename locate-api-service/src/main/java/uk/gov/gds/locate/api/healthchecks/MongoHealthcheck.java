package uk.gov.gds.locate.api.healthchecks;

import com.mongodb.MongoClient;
import com.yammer.metrics.core.HealthCheck;

public class MongoHealthcheck extends HealthCheck {

    private final MongoClient mongo;

    public MongoHealthcheck(MongoClient mongo) {
        super("MongoHealthcheck");
        this.mongo = mongo;
    }

    @Override
    protected Result check() throws Exception {
        mongo.getDatabaseNames().contains("locate");
        return Result.healthy();
    }
}
