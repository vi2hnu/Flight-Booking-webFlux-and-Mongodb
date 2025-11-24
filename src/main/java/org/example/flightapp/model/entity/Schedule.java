package org.example.flightapp.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.flightapp.DTO.ScheduleDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "Schedule")
@Data
@NoArgsConstructor
public class Schedule {

    @Id
    private String id;
    private String flightName;
    private String airlineName;
    private String fromCityAirportCode;
    private String toCityAirportCode;
    private LocalDate departureDate;
    private LocalDateTime departureTime;
    private float price;
    private int seatsAvailable;
    private int duration;

    public Schedule(ScheduleDTO scheduleDTO) {
        this.flightName = scheduleDTO.flightName();
        this.airlineName = scheduleDTO.airlineName();
        this.fromCityAirportCode = scheduleDTO.fromCityAirportCode();
        this.toCityAirportCode = scheduleDTO.toCityAirportCode();
        this.departureDate = scheduleDTO.departureDate();
        this.departureTime = scheduleDTO.departureTime();
        this.price = scheduleDTO.price();
        this.duration = scheduleDTO.duration();
    }
}
