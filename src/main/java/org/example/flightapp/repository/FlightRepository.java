package org.example.flightapp.repository;


import org.example.flightapp.model.entity.Flight;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FlightRepository extends ReactiveMongoRepository<Flight, Long> {

}
