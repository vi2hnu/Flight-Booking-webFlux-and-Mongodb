package org.example.flightapp.DTO;

import jakarta.validation.constraints.NotBlank;
import org.example.flightapp.model.enums.Gender;
import org.example.flightapp.model.enums.Meal;

public record PassengerDTO(
        @NotBlank
        String name,

        @NotBlank
        Gender gender,

        @NotBlank
        Meal meal,

        @NotBlank
        String seatPos) {
}