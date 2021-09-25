package edu.buffalo.ds.database;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Slf4j
public class MongoDBFactoryConnection {
    private MongoDBConnection mongoDBConnection;

    public MongoClient getClient()
    {
        MongoCredential mongoCredential = MongoCredential.createCredential(mongoDBConnection.getUsername(),
                mongoDBConnection.getDatabase(), mongoDBConnection.getPassword().toCharArray());
        return MongoClients.create(MongoClientSettings.builder()
                .credential(mongoCredential)
                .applyToClusterSettings(builder -> builder.hosts(getServers())).build());
    }

    private List<ServerAddress> getServers()
    {
        final List<Seed> seeds = mongoDBConnection.getSeeds();
        log.info(seeds.toString());
        return seeds.stream()
                .map(seed -> new ServerAddress(seed.getHost(), Integer.parseInt(seed.getPort())))
                .collect(Collectors.toList());
    }
}
