package org.example.flightapp.repository;


import org.example.flightapp.model.entity.City;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CityRepository extends ReactiveMongoRepository<City, String> {
    Mono<City> findCityByCityName(String cityName);
}
