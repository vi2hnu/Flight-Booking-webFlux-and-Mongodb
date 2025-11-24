package org.example.flightapp.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection ="BookedSeats")
@Data
public class BookedSeats {
    @Id
    private String id;
    private String seatPos;
    private String scheduleId;

    public BookedSeats(String scheduleId, String seatPos) {
        this.scheduleId = scheduleId;
        this.seatPos = seatPos;
    }
}