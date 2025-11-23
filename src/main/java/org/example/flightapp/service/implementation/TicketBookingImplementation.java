package org.example.flightapp.service.implementation;

import org.example.flightapp.DTO.TicketBookingDTO;
import org.example.flightapp.model.entity.Ticket;
import org.example.flightapp.service.TicketBookingInterface;
import reactor.core.publisher.Mono;

public class TicketBookingImplementation implements TicketBookingInterface {

    @Override
    public Mono<Ticket> bookTicket(TicketBookingDTO ticketBookingDTO) {
        return null;
    }
}
