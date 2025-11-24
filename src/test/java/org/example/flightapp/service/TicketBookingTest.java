package org.example.flightapp.service;

import org.example.flightapp.exception.TicketCancellationException;
import org.example.flightapp.exception.TicketNotFoundException;
import org.example.flightapp.model.entity.Passenger;
import org.example.flightapp.model.entity.Schedule;
import org.example.flightapp.model.entity.Ticket;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketBookingTest {

    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private PassengerRepository passengerRepository;
    @Mock
    private BookedSeatsRepository bookedSeatsRepository;

    @InjectMocks
    private TicketBookingImplementation ticketBooking;

    @Test
    void ticketBooking_getHistory() {
        String email = "xyz@gmail.com";
        Ticket ticket = new Ticket();
        ticket.setPnr("123");
        ticket.setBookedByUserEmail(email);
        when(ticketRepository.findTicketsByBookedByUserEmail(email))
                .thenReturn(Flux.just(ticket));

        StepVerifier.create(ticketBooking.getTicketHistory(email))
                .expectNext(ticket)
                .verifyComplete();
    }

    @Test
    void ticketCancel_throwsTicketNotFoundException() {
        String pnr = "123";
        when(ticketRepository.findTicketByPnr(pnr)).thenReturn(Mono.empty());
        StepVerifier.create(ticketBooking.deleteTicket(pnr))
                .expectError(TicketNotFoundException.class)
                .verify();
    }

    @Test
    void ticketCancel_throwsTicketCancellationException() {
        Schedule schedule = new Schedule();
        schedule.setId("321");
        schedule.setDepartureDate(LocalDate.now());
        schedule.setDepartureTime(LocalDateTime.now().plusMinutes(120));

        Ticket ticket = new Ticket();
        ticket.setPnr("123");
        ticket.setSchedule(schedule);

        when(ticketRepository.findTicketByPnr("123")).thenReturn(Mono.just(ticket));
        when(scheduleRepository.findScheduleById("321")).thenReturn(Mono.just(schedule));

        StepVerifier.create(ticketBooking.deleteTicket("123"))
                .expectError(TicketCancellationException.class)
                .verify();
    }

    @Test
    void deleteTicket_success() {
        String pnr = "PNR123";

        Ticket ticket = new Ticket();
        ticket.setPnr(pnr);
        ticket.setStatus(Status.BOOKED);

        Schedule schedule = new Schedule();
        schedule.setId("123");
        schedule.setSeatsAvailable(20);
        schedule.setDepartureTime(LocalDateTime.now().plusHours(30));

        Passenger passenger1 = new Passenger();
        passenger1.setSeatPosition("A1");

        Passenger passenger2 = new Passenger();
        passenger2.setSeatPosition("A2");

        ticket.setSchedule(schedule);
        ticket.setPassengers(List.of(passenger1, passenger2));

        when(ticketRepository.findTicketByPnr(pnr))
                .thenReturn(Mono.just(ticket));

        when(scheduleRepository.findScheduleById("123"))
                .thenReturn(Mono.just(schedule));

        when(ticketRepository.save(ticket))
                .thenReturn(Mono.just(ticket));

        when(scheduleRepository.save(schedule))
                .thenReturn(Mono.just(schedule));

        when(bookedSeatsRepository.deleteByScheduleIdAndSeatPos("123", "A1"))
                .thenReturn(Mono.empty());

        when(bookedSeatsRepository.deleteByScheduleIdAndSeatPos("123", "A2"))
                .thenReturn(Mono.empty());

        when(passengerRepository.delete(passenger1))
                .thenReturn(Mono.empty());

        when(passengerRepository.delete(passenger2))
                .thenReturn(Mono.empty());

        StepVerifier.create(ticketBooking.deleteTicket(pnr))
                .verifyComplete();
    }

}
