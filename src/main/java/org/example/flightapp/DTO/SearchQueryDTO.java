package org.example.flightapp.DTO;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SearchQueryDTO(
        @NotNull
        String fromCityAirportCode,

        @NotNull
        String toCityAirportCode,

        @NotNull
        LocalDate date) {

}