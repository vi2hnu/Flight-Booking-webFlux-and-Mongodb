package org.example.flightapp.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Year;

@Document(collection = "Flight")
@Data
public class Flight {
    @Id
    private String flightId;
    private String name;
    private Year year;
    private int rows;
    private int columns;
    private String airLineName;
}
