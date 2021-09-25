package edu.buffalo.ds.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.buffalo.ds.database.MongoDBConnection;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ApplicationConfiguration extends Configuration {

    @NotNull
    private MongoDBConnection mongoDBConnection;

    @JsonProperty
    public MongoDBConnection getMongoDBConnection()
    {
        return mongoDBConnection;
    }
    @JsonProperty
    public void setMongoDBConnection(MongoDBConnection mongoDBConnection)
    {
        this.mongoDBConnection = mongoDBConnection;
    }

}
