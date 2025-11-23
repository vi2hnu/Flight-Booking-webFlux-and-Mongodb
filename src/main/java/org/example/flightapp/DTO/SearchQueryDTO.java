package org.example.flightapp.DTO;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SearchQueryDTO(
        @NotNull
        String fromCity,

        @NotNull
        String toCity,

        @NotNull
        LocalDate date) {

}