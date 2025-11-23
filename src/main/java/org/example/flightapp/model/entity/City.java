package org.example.flightapp.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "City")
@Data
public class City {
    @Id
    private String id;
    private String cityName;

    @Indexed(unique = true)
    private String airportCode;
}
