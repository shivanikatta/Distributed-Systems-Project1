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
        environment.jersey().register(resource);
        environment.lifecycle().manage(mongoDBManaged);
    }
}
