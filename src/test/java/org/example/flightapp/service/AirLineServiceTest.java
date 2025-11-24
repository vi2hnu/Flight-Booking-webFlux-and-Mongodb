package org.example.flightapp.service;

import org.example.flightapp.DTO.ScheduleDTO;
import org.example.flightapp.exception.FlightNotFoundException;
import org.example.flightapp.exception.ScheduleConflictException;
import org.example.flightapp.model.entity.Flight;
import org.example.flightapp.model.entity.Schedule;
import org.example.flightapp.repository.FlightRepository;
import org.example.flightapp.repository.ScheduleRepository;
import org.example.flightapp.service.implementation.AirLineImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AirLineServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private AirLineImplementation airLineImplementation;

    private ScheduleDTO scheduleDTOObject(){
        return  new ScheduleDTO("Test Flight",
            "Fake Airlines",
            "MAA",
            "DEL",
            LocalDate.now(),
            LocalDateTime.now(),
            4311,
            120);
    }

    @Test
    void addSchedule_throwsFlightNotFound(){
        Flight flight = new Flight();
        flight.setName("Test Flight");
        ScheduleDTO schedule = scheduleDTOObject();
        when(flightRepository.findFlightByName("Test Flight"))
                .thenReturn(Mono.empty());

        StepVerifier.create(airLineImplementation.addSchedule(schedule))
                .expectError(FlightNotFoundException.class)
                .verify();
    }

    @Test
    void addSchedule_throwsScheduleConflict(){
        Flight flight = new Flight();
        flight.setName("Test Flight");
        ScheduleDTO schedule = scheduleDTOObject();
        when(flightRepository.findFlightByName("Test Flight"))
                .thenReturn(Mono.just(flight));
        when(scheduleRepository.findScheduleByFlightNameAndDepartureDate(flight.getName(), schedule.departureDate()))
                .thenReturn(Flux.just(new Schedule(schedule)));

        StepVerifier.create(airLineImplementation.addSchedule(schedule))
                .expectError(ScheduleConflictException.class)
                .verify();
    }

    @Test
    void addSchedule_success(){
        Flight flight = new Flight();
        flight.setName("Test Flight");
        ScheduleDTO schedule = scheduleDTOObject();
        when(flightRepository.findFlightByName("Test Flight"))
            .thenReturn(Mono.just(flight));
        when(scheduleRepository.findScheduleByFlightNameAndDepartureDate(flight.getName(), schedule.departureDate()))
                .thenReturn(Flux.empty());

        when(scheduleRepository.save(new Schedule(schedule)))
                .thenReturn(Mono.just(new Schedule(schedule)));

        StepVerifier.create(airLineImplementation.addSchedule(schedule))
                .expectNextMatches(saved ->
                        saved.getFlightName().equals("Test Flight")
                )
                .verifyComplete();
    }
}
