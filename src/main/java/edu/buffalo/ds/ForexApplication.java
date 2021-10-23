package edu.buffalo.ds;


import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import edu.buffalo.ds.configuration.ApplicationConfiguration;
import edu.buffalo.ds.database.MongoDBFactoryConnection;
import edu.buffalo.ds.database.MongoDBManaged;
import edu.buffalo.ds.database.daos.PublisherDAO;
import edu.buffalo.ds.database.daos.SubscriberDAO;
import edu.buffalo.ds.database.daos.TopicDAO;
import edu.buffalo.ds.database.daos.UserInfoDAO;
import edu.buffalo.ds.resources.ForexResource;
import edu.buffalo.ds.service.PublisherService;
import edu.buffalo.ds.service.SubscriberService;
import edu.buffalo.ds.service.TopicService;
import edu.buffalo.ds.service.UserInfoService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

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
        MongoClient mongoClient = mongoDBManagerConn.getClient();
        final MongoDBManaged mongoDBManaged = new MongoDBManaged(mongoClient);
        final UserInfoDAO userInfoDAO = new UserInfoDAO(mongoDBManagerConn);
        final SubscriberDAO subscriberDAO = new SubscriberDAO(mongoDBManagerConn);
        final TopicDAO topicDAO = new TopicDAO(mongoDBManagerConn);
        final PublisherDAO publisherDAO =  new PublisherDAO(mongoDBManagerConn);
        final ForexResource resource = new ForexResource(
                new UserInfoService(userInfoDAO)
                , new PublisherService(publisherDAO, topicDAO)
                , new SubscriberService(topicDAO, subscriberDAO)
                , new TopicService(topicDAO, subscriberDAO)
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
