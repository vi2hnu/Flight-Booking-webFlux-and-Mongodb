package org.example.flightapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.flightapp.model.entity.Ticket;
import org.example.flightapp.service.TicketDetailsInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1.0/flight/ticket")
public class TicketController {

    private final TicketDetailsInterface ticketDetailsInterface;
    public TicketController(TicketDetailsInterface ticketDetailsInterface) {
        this.ticketDetailsInterface = ticketDetailsInterface;
    }

    @GetMapping("/{pnr}")
    public Mono<ResponseEntity<Ticket>> getTicket(@PathVariable("pnr") String pnr) {
        log.info(pnr);
        return ticketDetailsInterface.getTicketDetails(pnr).map(saved -> ResponseEntity
                                                        .status(HttpStatus.OK)
                                                        .body(saved));
    }
}
