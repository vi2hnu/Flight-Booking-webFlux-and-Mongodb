package org.example.flightapp.service.implementation;

import org.example.flightapp.model.entity.Schedule;
import org.example.flightapp.repository.ScheduleRepository;
import org.example.flightapp.service.AirLineInterface;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AirLineImplementation implements AirLineInterface {


    private final ScheduleRepository scheduleRepository;

    public AirLineImplementation(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Mono<Schedule> addSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }
}
