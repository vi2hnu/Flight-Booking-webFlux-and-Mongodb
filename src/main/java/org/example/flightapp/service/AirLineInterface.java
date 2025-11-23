package org.example.flightapp.service;

import org.example.flightapp.DTO.ScheduleDTO;
import org.example.flightapp.model.entity.Schedule;
import reactor.core.publisher.Mono;

public interface AirLineInterface {
    Mono<Schedule> addSchedule(ScheduleDTO scheduleDTO);
}
