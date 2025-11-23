package org.example.flightapp.repository;

import org.example.flightapp.model.entity.Schedule;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface ScheduleRepository extends ReactiveMongoRepository<Schedule, Long> {
    Flux<Schedule> findScheduleByFromCityNameAndToCityNameAndDepartureDate(String fromCity, String toCity,LocalDate date);
    Flux<Schedule> findByFlightIdAndDepartureDate(String flightId,LocalDate date);
}
