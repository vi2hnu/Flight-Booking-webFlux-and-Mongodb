package org.example.flightapp.repository;

import org.example.flightapp.model.entity.BookedSeats;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface BookedSeatsRepository extends ReactiveMongoRepository<BookedSeats,String> {

}
