package org.example.flightapp.controller;

import org.example.flightapp.model.entity.Ticket;
import org.example.flightapp.service.TicketBookingInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;


@WebFluxTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private TicketBookingInterface ticketBookingInterface;

    @Test
    void getBookingHistory(){
        Ticket ticket = new Ticket();
        ticket.setPnr("123");
        String emailId = "xyz@gmail.com";
        when(ticketBookingInterface.getTicketHistory(emailId)).thenReturn(Flux.just(ticket));

        webTestClient.get()
                .uri("/api/v1.0/flight/booking/history/{emailId}", emailId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Ticket.class)
                .hasSize(1)
                .contains(ticket);
    }


    @Test
    void cancelTicket(){
        String pnr = "123";

        when(ticketBookingInterface.deleteTicket(pnr)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1.0/flight/booking/cancel/{pnr}", pnr)
                .exchange()
                .expectStatus().isNoContent();
    }
}
