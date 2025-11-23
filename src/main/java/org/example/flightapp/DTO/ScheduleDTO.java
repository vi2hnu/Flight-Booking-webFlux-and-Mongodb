package org.example.flightapp.DTO;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ScheduleDTO(

        @NotBlank
        String flightName,

        @NotBlank
        String airlineName,

        @NotBlank
        String fromCityAirportCode,

        @NotBlank
        String toCityAirportCode,

        @NotNull
        LocalDate departureDate,

        @NotNull
        LocalDateTime departureTime,

        @Positive
        float price,

        @Positive
        int duration
) {}
