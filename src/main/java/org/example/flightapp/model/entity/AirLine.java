package org.example.flightapp.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Airline")
@Data
public class AirLine {
    @Id
    private String id;

    @Indexed(unique = true)
    private String airlineName;
}
