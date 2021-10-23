package edu.buffalo.ds.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishRequest {
    private String topicName;
    private String rate;
}
