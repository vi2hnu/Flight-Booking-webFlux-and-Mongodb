package org.example.flightapp.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.flightapp.DTO.ScheduleDTO;
import org.example.flightapp.exception.ScheduleConflictException;
import org.example.flightapp.model.entity.Flight;
import org.example.flightapp.model.entity.Schedule;
import org.example.flightapp.repository.FlightRepository;
import org.example.flightapp.repository.ScheduleRepository;
import org.example.flightapp.service.AirLineInterface;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AirLineImplementation implements AirLineInterface {


    private final ScheduleRepository scheduleRepository;
    private final FlightRepository flightRepository;

    public AirLineImplementation(ScheduleRepository scheduleRepository, FlightRepository flightRepository) {
        this.scheduleRepository = scheduleRepository;
        this.flightRepository = flightRepository;
    }

    private Schedule toEntity(ScheduleDTO dto) {
        Schedule schedule = new Schedule();
        schedule.setFlightId(dto.flightId());
        schedule.setFlightName(dto.flightName());
        schedule.setAirlineName(dto.airlineName());
        schedule.setFromCityId(dto.fromCityId());
        schedule.setFromCityName(dto.fromCityName());
        schedule.setToCityId(dto.toCityId());
        schedule.setToCityName(dto.toCityName());
        schedule.setDepartureDate(dto.departureDate());
        schedule.setDepartureTime(dto.departureTime());
        schedule.setPrice(dto.price());
        schedule.setSeatsAvailable(dto.seatsAvailable());
        schedule.setDuration(dto.duration());
        return schedule;
    }

    @Override
    public Mono<Schedule> addSchedule(ScheduleDTO dto) {
        Schedule schedule = toEntity(dto);

        LocalDateTime newStart = schedule.getDepartureTime();
        LocalDateTime newEnd = newStart.plusMinutes(schedule.getDuration());
        Mono<Flight> flight = flightRepository.getFlightByFlightId(dto.flightId());
        // if no flight throw error
        int numberOfSeats = flight.block().getRows() * flight.block().getColumns();
        return scheduleRepository
                .findByFlightIdAndDepartureDate(schedule.getFlightId(), schedule.getDepartureDate())
                .collectList()
                //validation of conflict
                .flatMap(existingSchedules -> {

                    boolean conflict = existingSchedules.stream().anyMatch(s -> {
                        LocalDateTime existingStart = s.getDepartureTime();
                        LocalDateTime existingEnd = existingStart.plusMinutes(s.getDuration());

                        //checking if a schedule for the flight already exists at the requested time
                        return newStart.isBefore(existingEnd) && existingStart.isBefore(newEnd);
                    });

                    if (conflict) {
                        log.error("Schedule already exists");
                        return Mono.error(new ScheduleConflictException(
                                "Conflict: schedule overlaps with existing flight timings."
                        ));
                    }

                    return scheduleRepository.save(schedule);
                });
    }
}
