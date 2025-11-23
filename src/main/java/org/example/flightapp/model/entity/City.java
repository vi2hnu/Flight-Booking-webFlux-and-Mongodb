package org.example.flightapp.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "City")
@Data
public class City {
    @Id
    private String id;
    private String cityName;
    private String airportCode;
}
