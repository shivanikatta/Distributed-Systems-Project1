package edu.buffalo.ds.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Topic {
    @BsonProperty("id")
    private String topicId;
    @BsonProperty("name")
    private String topicName;
    @BsonProperty("subscribed")
    private String subscribed;
    @BsonProperty("message")
    private String message;
}
