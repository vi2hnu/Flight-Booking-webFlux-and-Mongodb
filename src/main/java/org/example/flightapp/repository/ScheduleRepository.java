package org.example.flightapp.repository;

import org.example.flightapp.model.entity.Schedule;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ScheduleRepository extends ReactiveMongoRepository<Schedule, Long> {
}
