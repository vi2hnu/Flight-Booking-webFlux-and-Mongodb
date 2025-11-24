package org.example.flightapp.service;

import org.example.flightapp.DTO.TicketBookingDTO;
import org.example.flightapp.model.entity.Ticket;
import reactor.core.publisher.Mono;

public interface TicketDetailsInterface {
    Mono<Ticket> getTicketDetails(String pnr);
}
