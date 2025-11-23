package org.example.flightapp.repository;

import org.example.flightapp.model.entity.Schedule;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface ScheduleRepository extends ReactiveMongoRepository<Schedule, Long> {
    Flux<Schedule> findScheduleByFromCityAirportCodeAndToCityAirportCodeAndDepartureDate(String fromCityAirportCode, String toCityAirportCode, LocalDate date);
    Flux<Schedule> findScheduleByFlightNameAndDepartureDate(String flightName,LocalDate date);
}
