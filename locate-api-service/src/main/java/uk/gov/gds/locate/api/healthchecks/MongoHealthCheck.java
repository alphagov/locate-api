package uk.gov.gds.locate.api.healthchecks;

import com.mongodb.MongoClient;
import com.yammer.metrics.core.HealthCheck;

public class MongoHealthCheck extends HealthCheck {

    private final MongoClient mongo;

    public MongoHealthCheck(MongoClient mongo) {
        super("MongoHealthCheck");
        this.mongo = mongo;
    }

    @Override
    protected Result check() throws Exception {
        mongo.getDatabaseNames().contains("locate");
        return Result.healthy();
    }
}