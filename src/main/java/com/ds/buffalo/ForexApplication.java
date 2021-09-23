package com.ds.buffalo;


import com.ds.buffalo.configuration.ApplicationConfiguration;
import com.ds.buffalo.resources.ForexResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class ForexApplication extends Application<ApplicationConfiguration> {
    public static void main(String[] args) throws Exception {
        new ForexApplication().run(args);
    }

    @Override
    public void run(ApplicationConfiguration applicationConfiguration, Environment environment) throws Exception {
        final ForexResource resource = new ForexResource(
                applicationConfiguration.getDefaultName()
        );
        environment.jersey().register(resource);
    }
}
