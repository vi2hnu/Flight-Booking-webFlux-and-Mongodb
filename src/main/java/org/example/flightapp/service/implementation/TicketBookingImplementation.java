package org.example.flightapp.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.flightapp.DTO.TicketBookingDTO;
import org.example.flightapp.exception.*;
import org.example.flightapp.model.entity.BookedSeats;
import org.example.flightapp.model.entity.Passenger;
import org.example.flightapp.model.entity.Schedule;
import org.example.flightapp.model.entity.Ticket;
import org.example.flightapp.model.enums.Status;
import org.example.flightapp.repository.BookedSeatsRepository;
import org.example.flightapp.repository.PassengerRepository;
import org.example.flightapp.repository.ScheduleRepository;
import org.example.flightapp.repository.TicketRepository;
import org.example.flightapp.service.TicketBookingInterface;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TicketBookingImplementation implements TicketBookingInterface {
    /*
        1) get the schedule id and check if seats are available
        2) reduce the number of seats
        3) add passengers and their details
        4) add booked seats
        5) create ticket
     */

    private final ScheduleRepository scheduleRepository;
    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final BookedSeatsRepository bookedSeatsRepository;

    public TicketBookingImplementation(ScheduleRepository scheduleRepository, TicketRepository ticketRepository,
                                       PassengerRepository passengerRepository, BookedSeatsRepository bookedSeatsRepository) {
        this.scheduleRepository = scheduleRepository;
        this.ticketRepository = ticketRepository;
        this.passengerRepository = passengerRepository;
        this.bookedSeatsRepository = bookedSeatsRepository;
    }

    @Override
    public Mono<Ticket> bookTicket(TicketBookingDTO ticketBookingDTO) {
        int ticketsNeeded = ticketBookingDTO.passengers().size();

        //validation for checking the schedule
        return scheduleRepository.findScheduleById(ticketBookingDTO.scheduleId())
                .switchIfEmpty(
                        Mono.defer(() -> {
                            log.error("Schedule not found: " + ticketBookingDTO.scheduleId());
                            return Mono.error(new ScheduleNotFoundException("Schedule not found: " + ticketBookingDTO.scheduleId()));
                        })
                )
                //checking the there are enough seats available
                .flatMap(
                schedule -> {
                            if (ticketsNeeded > schedule.getSeatsAvailable()) {
                                return Mono.defer(() -> {
                                    log.error("Not enough seats available. Requested: " + ticketsNeeded +
                                            ", Available: " + schedule.getSeatsAvailable());
                                    return Mono.error(new NotEnoughSeatsException(
                                            "Not enough seats available. Requested: " + ticketsNeeded +
                                                    ", Available: " + schedule.getSeatsAvailable()
                                    ));
                                });
                            }
                            //checking if the selected seats are booked
                            return Flux.fromIterable(ticketBookingDTO.passengers())
                                    .flatMap(passenger -> bookedSeatsRepository
                                            .existsByScheduleIdAndSeatPos(schedule.getId(), passenger.seatPos())
                                            .filter(isBooked -> isBooked)
                                            .map(isBooked -> passenger.seatPos()))
                                    .collectList()
                                    .flatMap(conflictingSeats -> {
                                        if (!conflictingSeats.isEmpty()) {
                                            log.error("Conflicting seats: " + conflictingSeats);
                                            return Mono.error(new SeatsAlreadyBookedException(
                                                    "The following seats are already booked: " + conflictingSeats));
                                        }
                                        return Mono.just(schedule);
                                    });
                })
                //ticket booking part
                .flatMap(schedule -> {
                    schedule.setSeatsAvailable(schedule.getSeatsAvailable() - ticketsNeeded);

                    // save schedule
                    return scheduleRepository.save(schedule)
                            .flatMap(savedSchedule -> {

                                // create a ticket object
                                Ticket ticket = new Ticket();
                                long randomNumber = 1_000_000_000L + (long)(Math.random() * 9_000_000_000L);
                                String pnr = "PNR" + randomNumber;
                                ticket.setPnr(pnr);
                                ticket.setBookedByUserEmail(ticketBookingDTO.userEmail());
                                ticket.setSchedule(savedSchedule);
                                ticket.setReturnTrip(null);
                                ticket.setStatus(Status.BOOKED);

                                List<Passenger> passengers = ticketBookingDTO.passengers().stream()
                                        .map(Passenger::new)
                                        .toList();
                                ticket.setPassengers(passengers);

                                // save passengers and booked seats
                                return Flux.fromIterable(passengers)
                                        .flatMap(passenger -> {
                                            Mono<Void> seatSave = bookedSeatsRepository.save(new BookedSeats(savedSchedule.getId(), passenger.getSeatPosition())).then();
                                            Mono<Passenger> passengerSave = passengerRepository.save(passenger);
                                            return seatSave.then(passengerSave);
                                        })
                                .then(ticketRepository.save(ticket));
                            });
                });
    }

    @Override
    public Flux<Ticket> getTicketHistory(String email) {
        return ticketRepository.findTicketsByBookedByUserEmail(email);
    }

    @Override
    public Mono<Void> deleteTicket(String pnr) {
        return ticketRepository.findTicketByPnr(pnr)
                //ticket validation
                .switchIfEmpty(Mono.error(new TicketNotFoundException("Ticket not found")))
                .flatMap(ticket ->
                        scheduleRepository.findScheduleById(ticket.getSchedule().getId())
                                .flatMap(schedule -> {
                                    long timeDifference = Duration.between(LocalDateTime.now(), schedule.getDepartureTime()).toHours();

                                    //checking if the user is trying to cancel ticket within 24 hours window
                                    if(timeDifference < 24){
                                        return Mono.error(new TicketCancellationException("Can't delete ticket within 24 hours"));
                                    }

                                    //setting the status of ticket and deleting passengers and seats booked
                                    ticket.setStatus(Status.CANCELLED);

                                    Mono<Ticket> ticketSave = ticketRepository.save(ticket);
                                    schedule.setSeatsAvailable(schedule.getSeatsAvailable() + ticket.getPassengers().size());
                                    Mono<Schedule> scheduleSave = scheduleRepository.save(schedule);

                                    Mono<Void> passengersAndSeats =
                                            Flux.fromIterable(ticket.getPassengers())
                                                    .flatMap(passenger ->
                                                            bookedSeatsRepository
                                                                    .deleteByScheduleIdAndSeatPos(schedule.getId(), passenger.getSeatPosition())
                                                                    .then(passengerRepository.delete(passenger))
                                                    )
                                                    .then();

                                    return Mono.when(ticketSave, scheduleSave, passengersAndSeats);
                                })
                )
                .then();
    }

}
