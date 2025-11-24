package org.example.flightapp.controller;

import org.example.flightapp.model.entity.Ticket;
import org.example.flightapp.service.TicketDetailsInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;


@WebFluxTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private TicketDetailsInterface ticketDetailsInterface;

    @Test
    void getTicketDetails() {
        String pnr = "123";
        Ticket ticket = new Ticket();
        ticket.setPnr(pnr);

        when(ticketDetailsInterface.getTicketDetails(pnr)).thenReturn(Mono.just(ticket));

        webTestClient.get()
                .uri("/api/v1.0/flight/ticket/{pnr}",pnr)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Ticket.class)
                .isEqualTo(ticket);

    }
}
