package edu.buffalo.ds.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class MongoDBConnection {
    @JsonProperty
    private String username;
    @JsonProperty
    private String password;
    @JsonProperty
    private String database;
    @JsonProperty
    private List<Seed> seeds;
}
