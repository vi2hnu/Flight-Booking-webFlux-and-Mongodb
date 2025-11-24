package org.example.flightapp.service;

import org.example.flightapp.exception.TicketNotFoundException;
import org.example.flightapp.model.entity.Ticket;
import org.example.flightapp.repository.TicketRepository;
import org.example.flightapp.service.implementation.TicketDetailsImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TicketDetailsTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketDetailsImplementation ticketDetailsImplementation;

    @Test
    void ticketDetails_throwsTicketNotFoundException() {
        String pnr = "12345";
        when(ticketRepository.findTicketByPnr(pnr))
                .thenReturn(Mono.empty());

        StepVerifier.create(ticketDetailsImplementation.getTicketDetails(pnr))
                .expectError(TicketNotFoundException.class)
                .verify();
    }

    @Test
    void ticketDetails_success() {
        String pnr = "12345";
        Ticket  ticket = new Ticket();
        ticket.setPnr(pnr);
        when(ticketRepository.findTicketByPnr(pnr))
                .thenReturn(Mono.just(ticket));

        StepVerifier.create(ticketDetailsImplementation.getTicketDetails(pnr))
                .expectNext(ticket)
                .verifyComplete();
    }
}
