package edu.buffalo.ds.database;

import lombok.Getter;

import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class MongoDBConnection {
    private String username;
    private String password;
    private String database;
    private List<Seed> seeds;
}
