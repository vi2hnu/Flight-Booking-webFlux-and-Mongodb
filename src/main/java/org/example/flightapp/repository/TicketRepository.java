package org.example.flightapp.repository;

import org.example.flightapp.model.entity.Ticket;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TicketRepository extends ReactiveMongoRepository<Ticket, Long> {
    Mono<Ticket> findTicketByPnr(String pnr);
    Flux<Ticket> findTicketsByBookedByUserEmail(String email);
}
