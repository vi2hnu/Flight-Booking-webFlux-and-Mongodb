package org.example.flightapp.service;

import org.example.flightapp.DTO.TicketBookingDTO;
import org.example.flightapp.model.entity.Ticket;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TicketBookingInterface {
    Mono<Ticket> bookTicket(TicketBookingDTO ticketBookingDTO);
    Flux<Ticket> getTicketHistory(String email);
    Mono<Void>   deleteTicket(String pnr);
}
