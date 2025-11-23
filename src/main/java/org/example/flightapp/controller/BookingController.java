package org.example.flightapp.controller;

import jakarta.validation.Valid;
import org.example.flightapp.DTO.TicketBookingDTO;
import org.example.flightapp.model.entity.Ticket;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1.0/flight/booking")
public class BookingController {

    @PostMapping("/{flightId}")
    public Ticket bookTicket(@Valid @RequestBody TicketBookingDTO ticketBookingDTO){
        return null;
    }
}
