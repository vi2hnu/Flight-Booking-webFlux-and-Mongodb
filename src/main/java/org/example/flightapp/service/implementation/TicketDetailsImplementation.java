package org.example.flightapp.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.flightapp.exception.TicketNotFoundException;
import org.example.flightapp.model.entity.Ticket;
import org.example.flightapp.repository.TicketRepository;
import org.example.flightapp.service.TicketDetailsInterface;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class TicketDetailsImplementation implements TicketDetailsInterface {

    private final TicketRepository  ticketRepository;
    public TicketDetailsImplementation(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Mono<Ticket> getTicketDetails(String pnr) {
        return ticketRepository.findTicketByPnr(pnr)
                .switchIfEmpty(
                        Mono.defer(() -> {
                            log.error("Ticket not found for pnr: "+pnr);
                            return Mono.error(new TicketNotFoundException("Ticket not found for pnr: "+pnr));
                        })
                );
    }
}
