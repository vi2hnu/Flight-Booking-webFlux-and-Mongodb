package org.example.flightapp.model.entity;

import lombok.Data;
import org.example.flightapp.model.enums.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "tickets")
@Data
public class Ticket {

    @Id
    private String id;

    @Indexed(unique = true)
    private String pnr;
    private Schedule schedule;
    private Schedule returnTrip;
    private String bookedByUserEmail;
    private List<Passenger> passengers = new ArrayList<>();
    private Status status;
}