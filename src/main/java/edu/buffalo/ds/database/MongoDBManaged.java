package edu.buffalo.ds.database;

import com.mongodb.client.MongoClient;
import io.dropwizard.lifecycle.Managed;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MongoDBManaged implements Managed {
    private MongoClient mongoClient;

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {
        getMongoClient().close();
    }
}
