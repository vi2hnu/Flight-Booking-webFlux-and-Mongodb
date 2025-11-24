package org.example.flightapp.model.entity;

import lombok.Data;
import org.example.flightapp.DTO.PassengerDTO;
import org.example.flightapp.model.enums.Gender;
import org.example.flightapp.model.enums.Meal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Passenger")
@Data
public class Passenger {
    @Id
    private String id;
    private String name;
    private Gender gender;
    private Meal mealOption;
    String seatPosition;  //eg.12A

    public Passenger(PassengerDTO passengerDTO) {
        this.name = passengerDTO.name();
        this.gender = passengerDTO.gender();
        this.mealOption = passengerDTO.meal();
        this.seatPosition = passengerDTO.seatPos();
    }
}
