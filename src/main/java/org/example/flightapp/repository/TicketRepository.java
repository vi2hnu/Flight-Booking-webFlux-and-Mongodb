package org.example.flightapp.repository;

import org.example.flightapp.model.entity.Ticket;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TicketRepository extends ReactiveMongoRepository<Ticket, Long> {
}
