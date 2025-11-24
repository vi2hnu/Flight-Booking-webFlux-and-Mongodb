package org.example.flightapp.service;


import org.example.flightapp.DTO.PassengerDTO;
import org.example.flightapp.DTO.TicketBookingDTO;
import org.example.flightapp.exception.NotEnoughSeatsException;
import org.example.flightapp.exception.ScheduleNotFoundException;
import org.example.flightapp.exception.SeatsAlreadyBookedException;
import org.example.flightapp.model.entity.BookedSeats;
import org.example.flightapp.model.entity.Passenger;
import org.example.flightapp.model.entity.Schedule;
import org.example.flightapp.model.entity.Ticket;
import org.example.flightapp.model.enums.Gender;
import org.example.flightapp.model.enums.Meal;
import org.example.flightapp.model.enums.Status;
import org.example.flightapp.repository.BookedSeatsRepository;
import org.example.flightapp.repository.PassengerRepository;
import org.example.flightapp.repository.ScheduleRepository;
import org.example.flightapp.repository.TicketRepository;
import org.example.flightapp.service.implementation.TicketBookingImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookSingleTicketTest {

    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private PassengerRepository passengerRepository;
    @Mock
    private BookedSeatsRepository bookedSeatsRepository;

    @InjectMocks
    private TicketBookingImplementation booking;

    private Passenger passenger(String seat) {
        Passenger p = new Passenger();
        p.setSeatPosition(seat);
        return p;
    }

    @Test
    void bookTicket_scheduleNotFound() {
        List<PassengerDTO> passengers = new ArrayList<>();
        TicketBookingDTO dto = new TicketBookingDTO(
                "user@gmail.com",
                "100",
                "",
                passengers
        );

        when(scheduleRepository.findScheduleById("100"))
                .thenReturn(Mono.empty());

        StepVerifier.create(booking.bookTicket(dto))
                .expectError(ScheduleNotFoundException.class)
                .verify();
    }

    @Test
    void bookTicket_notEnoughSeats() {
        List<PassengerDTO> passengers = new ArrayList<>();
        passengers.add(new PassengerDTO("A", Gender.MALE, Meal.VEG, "1A"));
        passengers.add(new PassengerDTO("B", Gender.FEMALE, Meal.NONVEG, "1B"));

        TicketBookingDTO dto = new TicketBookingDTO(
                "user@gmail.com",
                "123",
                "",
                passengers
        );

        Schedule schedule = new Schedule();
        schedule.setId("123");
        schedule.setSeatsAvailable(1);

        when(scheduleRepository.findScheduleById("123"))
                .thenReturn(Mono.just(schedule));

        StepVerifier.create(booking.bookTicket(dto))
                .expectError(NotEnoughSeatsException.class)
                .verify();
    }


    @Test
    void bookTicket_seatsAlreadyBooked() {
        List<PassengerDTO> passengers = new ArrayList<>();
        passengers.add(new PassengerDTO("A", Gender.MALE, Meal.VEG, "1A"));
        passengers.add(new PassengerDTO("B", Gender.FEMALE, Meal.NONVEG, "1B"));

        TicketBookingDTO dto = new TicketBookingDTO(
                "user@gmail.com",
                "123",
                "",
                passengers
        );

        Schedule schedule = new Schedule();
        schedule.setId("123");
        schedule.setSeatsAvailable(10);

        when(scheduleRepository.findScheduleById("123"))
                .thenReturn(Mono.just(schedule));

        when(bookedSeatsRepository.existsByScheduleIdAndSeatPos("123", "1A"))
                .thenReturn(Mono.just(true));

        when(bookedSeatsRepository.existsByScheduleIdAndSeatPos("123", "1B"))
                .thenReturn(Mono.just(false));

        StepVerifier.create(booking.bookTicket(dto))
                .expectError(SeatsAlreadyBookedException.class)
                .verify();
    }


    @Test
    void bookTicket_success() {
        List<PassengerDTO> passengers = new ArrayList<>();
        passengers.add(new PassengerDTO("A", Gender.MALE, Meal.VEG, "1A"));
        passengers.add(new PassengerDTO("B", Gender.FEMALE, Meal.NONVEG, "1B"));

        TicketBookingDTO dto = new TicketBookingDTO(
                "user@gmail.com",
                "123",
                "",
                passengers
        );

        Schedule schedule = new Schedule();
        schedule.setId("123");
        schedule.setSeatsAvailable(5);

        Passenger p1 = new Passenger(passengers.get(0));
        Passenger p2 = new Passenger(passengers.get(1));
        List<Passenger> passengersSaved = new ArrayList<>();
        passengersSaved.add(p1);
        passengersSaved.add(p2);

        when(scheduleRepository.findScheduleById("123"))
                .thenReturn(Mono.just(schedule));

        when(bookedSeatsRepository.existsByScheduleIdAndSeatPos("123", "1A"))
                .thenReturn(Mono.just(false));

        when(bookedSeatsRepository.existsByScheduleIdAndSeatPos("123", "1B"))
                .thenReturn(Mono.just(false));

        Schedule savedSchedule = new Schedule();
        savedSchedule.setId("123");
        savedSchedule.setSeatsAvailable(3);

        when(scheduleRepository.save(any(Schedule.class)))
                .thenReturn(Mono.just(savedSchedule));

        when(passengerRepository.save(any(Passenger.class)))
                .thenAnswer(i -> Mono.just(i.getArgument(0)));

        when(bookedSeatsRepository.save(any(BookedSeats.class)))
                .thenReturn(Mono.empty());

        Ticket savedTicket = new Ticket();
        savedTicket.setPnr("PNR123");
        savedTicket.setPassengers(passengersSaved);
        savedTicket.setSchedule(savedSchedule);
        savedTicket.setBookedByUserEmail("user@example.com");

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(Mono.just(savedTicket));

        StepVerifier.create(booking.bookTicket(dto))
                .expectNextMatches(t -> t.getPnr() != null && t.getSchedule().getSeatsAvailable() == 3)
                .verifyComplete();
    }


}
