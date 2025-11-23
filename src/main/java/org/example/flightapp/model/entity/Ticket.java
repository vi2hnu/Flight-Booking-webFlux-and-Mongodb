package org.example.flightapp.model.entity;

import lombok.Data;
import org.example.flightapp.model.enums.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "tickets")
@Data
public class Ticket {

    @Id
    private String id;

    private String pnr;
    private String bookedByUserId;
    private String scheduleId;
    private String returnTripId;

    private List<Passenger> passengers = new ArrayList<>();

    private Status status;
}