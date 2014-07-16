package uk.gov.gds.locate.api;

import com.sun.jersey.api.core.ResourceConfig;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import uk.gov.gds.locate.api.configuration.LocateApiConfiguration;
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
        removeDefaultExceptionMappers(environment);
        environment.addResource(new AddressResource());
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
