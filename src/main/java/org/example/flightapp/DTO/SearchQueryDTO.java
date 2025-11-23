package org.example.flightapp;

import java.time.LocalDate;

public record SearchQueryDTO(String fromCity, String toCity, LocalDate date) {

}