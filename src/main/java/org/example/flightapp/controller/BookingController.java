package org.example.flightapp.controller;

import jakarta.validation.Valid;
import org.example.flightapp.DTO.TicketBookingDTO;
import org.example.flightapp.model.entity.Ticket;
import org.example.flightapp.service.TicketBookingInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1.0/flight/booking")
public class BookingController {

    private final TicketBookingInterface ticketBookingInterface;
    public BookingController(TicketBookingInterface ticketBookingInterface) {
        this.ticketBookingInterface = ticketBookingInterface;
    }

    @PostMapping("/{flightId}")
    public Mono<ResponseEntity<Ticket>> bookTicket(@Valid @RequestBody TicketBookingDTO ticketBookingDTO){
        return ticketBookingInterface.bookTicket(ticketBookingDTO).map(saved -> ResponseEntity
                                                        .status(HttpStatus.CREATED)
                                                        .body(saved));
    }

    @GetMapping("/history/{emailId}")
    public Flux<Ticket> getBookingHistory(@PathVariable String emailId){
        return ticketBookingInterface.getTicketHistory(emailId);
    }
}
