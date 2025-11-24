package org.example.flightapp.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.example.flightapp.model.entity.Users;

import java.util.List;

public record TicketBookingDTO(
        @NotNull
        String userEmail,

        @NotNull
        String scheduleId,

        String returnTripId,

        @NotEmpty
        List<PassengerDTO>passengers) {

}