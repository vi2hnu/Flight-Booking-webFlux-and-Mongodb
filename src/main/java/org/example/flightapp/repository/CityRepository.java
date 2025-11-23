package org.example.flightapp.repository;


import org.example.flightapp.model.entity.City;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CityRepository extends ReactiveMongoRepository<City, String> {

}
