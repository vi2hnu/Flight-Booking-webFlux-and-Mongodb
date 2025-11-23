package org.example.flightapp.DTO;

import java.time.LocalDate;

public record SearchQueryDTO(String fromCity, String toCity, LocalDate date) {

}