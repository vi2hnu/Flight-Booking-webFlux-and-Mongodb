package org.example.flightapp.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "Schedule")
@Data
public class Schedule {

    @Id
    private String id;
    private String flightId;
    private String flightName;
    private String airlineName;
    private String fromCityId;
    private String fromCityName;
    private String toCityId;
    private String toCityName;
    private LocalDate departureDate;
    private LocalDateTime departureTime;
    private float price;
    private int seatsAvailable;
    private int duration;
}
