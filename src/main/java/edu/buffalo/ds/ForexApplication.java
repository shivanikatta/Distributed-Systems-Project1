package edu.buffalo.ds;


import edu.buffalo.ds.configuration.ApplicationConfiguration;
import edu.buffalo.ds.database.MongoDBFactoryConnection;
import edu.buffalo.ds.database.MongoDBManaged;
import edu.buffalo.ds.database.daos.UserInfoDAO;
import edu.buffalo.ds.resources.ForexResource;
import edu.buffalo.ds.service.UserInfoService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class ForexApplication extends Application<ApplicationConfiguration> {
    public static void main(String[] args) throws Exception {
        new ForexApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ApplicationConfiguration> bootstrap) {
        super.initialize(bootstrap);
    }

    @Override
    public void run(ApplicationConfiguration applicationConfiguration, Environment environment) throws Exception {
        final MongoDBFactoryConnection mongoDBManagerConn =
                new MongoDBFactoryConnection(applicationConfiguration.getMongoDBConnection());
        final MongoDBManaged mongoDBManaged = new MongoDBManaged(mongoDBManagerConn.getClient());
        final UserInfoDAO userInfoDAO = new UserInfoDAO(mongoDBManagerConn);
        final ForexResource resource = new ForexResource(
                new UserInfoService(userInfoDAO)
        );

        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        environment.jersey().register(resource);
        environment.lifecycle().manage(mongoDBManaged);
    }
}
