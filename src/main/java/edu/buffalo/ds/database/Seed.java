package edu.buffalo.ds.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class Seed {
    @JsonProperty
    private String host;
    @JsonProperty
    private String port;
}
