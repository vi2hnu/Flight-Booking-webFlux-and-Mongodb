package org.example.flightapp.repository;

import org.example.flightapp.model.entity.BookedSeats;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface BookedSeatsRepository extends ReactiveMongoRepository<BookedSeats,String> {
    Mono<Boolean> existsByScheduleIdAndSeatPos(String scheduleId, String seatPos);
    Mono<Void> deleteByScheduleIdAndSeatPos(String scheduleId, String seatPos);
}
