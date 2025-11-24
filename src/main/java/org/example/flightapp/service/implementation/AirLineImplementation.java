package org.example.flightapp.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.flightapp.DTO.ScheduleDTO;
import org.example.flightapp.exception.FlightNotFoundException;
import org.example.flightapp.exception.ScheduleConflictException;
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
        schedule.setFlightName(dto.flightName());
        schedule.setAirlineName(dto.airlineName());
        schedule.setFromCityAirportCode(dto.fromCityAirportCode());
        schedule.setToCityAirportCode(dto.toCityAirportCode());
        schedule.setDepartureDate(dto.departureDate());
        schedule.setDepartureTime(dto.departureTime());
        schedule.setPrice(dto.price());
        schedule.setDuration(dto.duration());
        return schedule;
    }

// previous approach which yielded wrong result, keeping for future references


//    @Override
//    public Mono<Schedule> addSchedule(ScheduleDTO dto) {
//        Schedule schedule = toEntity(dto);
//
//        LocalDateTime newStart = schedule.getDepartureTime();
//        LocalDateTime newEnd = newStart.plusMinutes(schedule.getDuration());
//        return scheduleRepository
//                .findScheduleByFlightNameAndDepartureDate(schedule.getFlightName(), schedule.getDepartureDate())
//                .collectList()
//                //validation of conflict
//                .flatMap(existingSchedules -> {
//
//                    boolean conflict = existingSchedules.stream().anyMatch(s -> {
//                        LocalDateTime existingStart = s.getDepartureTime();
//                        LocalDateTime existingEnd = existingStart.plusMinutes(s.getDuration());
//
//                        //checking if a schedule for the flight already exists at the requested time
//                        return newStart.isBefore(existingEnd) && existingStart.isBefore(newEnd);
//                    });
//
//                    if (conflict) {
//                        log.error("Schedule already exists");
//                        return Mono.error(new ScheduleConflictException(
//                                "Conflict: schedule overlaps with existing flight timings."
//                        ));
//                    }
//
//                    return scheduleRepository.save(schedule);
//                });
//    }

    @Override
    public Mono<Schedule> addSchedule(ScheduleDTO dto) {
        return flightRepository.findFlightByName(dto.flightName())
                //validation to check if the flight exists
                .switchIfEmpty(
                        Mono.defer(() -> {
                            log.error("Flight not found");
                            return Mono.error(new FlightNotFoundException("Flight not found"));
                        })
                )
                .flatMap(flight -> {
                    Schedule schedule = toEntity(dto);
                    schedule.setSeatsAvailable(flight.getRows() * flight.getColumns());
                    LocalDateTime newStart = schedule.getDepartureTime();
                    LocalDateTime newEnd = newStart.plusMinutes(schedule.getDuration());
                    return scheduleRepository
                            .findScheduleByFlightNameAndDepartureDate(schedule.getFlightName(), schedule.getDepartureDate())
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
                });
    }
}
