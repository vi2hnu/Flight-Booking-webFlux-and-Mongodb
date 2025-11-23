package org.example.flightapp.repository;


import org.example.flightapp.model.entity.AirLine;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AirLineRepository extends ReactiveMongoRepository<AirLine,String> {

}
