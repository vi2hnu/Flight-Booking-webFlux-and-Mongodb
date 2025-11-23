package org.example.flightapp.DTO;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ScheduleDTO(
        @NotBlank
        String flightId,

        @NotBlank
        String flightName,

        @NotBlank
        String airlineName,

        @NotBlank
        String fromCityId,

        @NotBlank
        String fromCityName,

        @NotBlank
        String toCityId,

        @NotBlank
        String toCityName,

        @NotNull
        LocalDate departureDate,

        @NotNull
        LocalDateTime departureTime,

        @Positive
        float price,

        @Positive
        int seatsAvailable,

        @Positive
        int duration
) {}
