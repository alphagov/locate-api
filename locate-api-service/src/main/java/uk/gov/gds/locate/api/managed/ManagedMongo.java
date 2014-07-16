package uk.gov.gds.locate.api.managed;

import com.mongodb.MongoClient;
import com.yammer.dropwizard.lifecycle.Managed;

public class ManagedMongo implements Managed {

    private final MongoClient mongo;

    public ManagedMongo(MongoClient mongo) {
        this.mongo = mongo;
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {
        mongo.close();
    }
}
