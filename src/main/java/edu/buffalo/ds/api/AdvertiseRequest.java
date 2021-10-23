package edu.buffalo.ds.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertiseRequest {
    private String topicName;
    private String publisherId;
}
