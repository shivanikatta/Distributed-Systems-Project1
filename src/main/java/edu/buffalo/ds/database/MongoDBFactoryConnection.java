package edu.buffalo.ds.database;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;
import java.util.stream.Collectors;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Getter
@AllArgsConstructor
@Slf4j
public class MongoDBFactoryConnection {
    private MongoDBConnection mongoDBConnection;

    public MongoClient getClient()
    {
        MongoCredential mongoCredential = MongoCredential.createCredential(mongoDBConnection.getUsername(),
                mongoDBConnection.getDatabase(), mongoDBConnection.getPassword().toCharArray());
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        return MongoClients.create(MongoClientSettings.builder()
                .credential(mongoCredential)
                .codecRegistry(codecRegistry)
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
