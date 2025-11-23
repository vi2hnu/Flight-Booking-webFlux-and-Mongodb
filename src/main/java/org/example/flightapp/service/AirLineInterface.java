package org.example.flightapp.service;

import org.example.flightapp.model.entity.Schedule;
import reactor.core.publisher.Mono;

public interface AirLineInterface {
    Mono<Schedule> addSchedule(Schedule schedule);
}
