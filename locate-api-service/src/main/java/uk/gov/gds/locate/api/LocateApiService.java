package uk.gov.gds.locate.api;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.sun.jersey.api.core.ResourceConfig;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import org.mongojack.JacksonDBCollection;
import uk.gov.gds.locate.api.configuration.LocateApiConfiguration;
import uk.gov.gds.locate.api.dao.AddressDao;
import uk.gov.gds.locate.api.healthchecks.MongoHealthcheck;
import uk.gov.gds.locate.api.managed.ManagedMongo;
import uk.gov.gds.locate.api.model.Address;
import uk.gov.gds.locate.api.resources.AddressResource;

import javax.ws.rs.ext.ExceptionMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LocateApiService extends Service<LocateApiConfiguration> {

    public static void main(String[] args) throws Exception {
        new LocateApiService().run(args);
    }

    @Override
    public void initialize(Bootstrap<LocateApiConfiguration> bootstrap) {

    }

    @Override
    public void run(LocateApiConfiguration configuration, Environment environment) throws Exception {
        Mongo mongo = new Mongo(configuration.getMongoConfiguration().getHosts(), configuration.getMongoConfiguration().getPort());
        environment.manage(new ManagedMongo(mongo));

        DB db = mongo.getDB(configuration.getMongoConfiguration().getDatabaseName());
        JacksonDBCollection<Address, String> addressCollection = JacksonDBCollection.wrap(db.getCollection("addresses"), Address.class, String.class);

        AddressDao addressDao = new AddressDao(addressCollection);

        environment.addResource(new AddressResource(addressDao));

        environment.addHealthCheck(new MongoHealthcheck(mongo));

        removeDefaultExceptionMappers(environment);
    }

    private void removeDefaultExceptionMappers(Environment environment) {
        List<Object> singletonsToRemove = new ArrayList<Object>();
        ResourceConfig jrConfig = environment.getJerseyResourceConfig();
        Set<Object> dwSingletons = jrConfig.getSingletons();

        for (Object s : dwSingletons) {
            if (s instanceof ExceptionMapper && s.getClass().getName().startsWith("com.yammer.dropwizard.jersey.")) {
                singletonsToRemove.add(s);
            }
        }
        for (Object s : singletonsToRemove) {
            jrConfig.getSingletons().remove(s);
        }

    }
}
