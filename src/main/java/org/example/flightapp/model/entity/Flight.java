package org.example.flightapp.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Flight")
@Data
public class    Flight {
    @Id
    private String flightId;

    @Indexed(unique = true)
    private String name;
    private int year;
    private int rows;
    private int columns;
    private String airLineName;
}
