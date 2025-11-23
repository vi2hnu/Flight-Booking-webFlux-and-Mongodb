package org.example.flightapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.flightapp.model.entity.Schedule;
import org.example.flightapp.repository.AirLineRepository;
import org.example.flightapp.repository.ScheduleRepository;
import org.example.flightapp.service.AirLineInterface;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1.0/flight/airline")
public class AirLineController {

    private final AirLineInterface airLineInterface;
    public AirLineController(AirLineInterface airLineInterface) {
        this.airLineInterface = airLineInterface;
    }

    @PostMapping("/inventory")
    public Mono<Schedule> addSchedule(@RequestBody Schedule schedule){
        return airLineInterface.addSchedule(schedule);
    }

}
