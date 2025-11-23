package org.example.flightapp.repository;

import org.example.flightapp.model.entity.Passenger;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public  interface PassengerRepository  extends ReactiveMongoRepository<Passenger, Long> {
}
