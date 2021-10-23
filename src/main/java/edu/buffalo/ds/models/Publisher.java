package edu.buffalo.ds.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Publisher {
    private String publisherId;
    private List<String> topics;
}
