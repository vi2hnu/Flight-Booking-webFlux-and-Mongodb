package org.example.flightapp.service;

import org.example.flightapp.DTO.ScheduleDTO;
import org.example.flightapp.DTO.SearchQueryDTO;
import org.example.flightapp.exception.CityNotFoundException;
import org.example.flightapp.model.entity.City;
import org.example.flightapp.model.entity.Schedule;
import org.example.flightapp.repository.CityRepository;
import org.example.flightapp.repository.ScheduleRepository;
import org.example.flightapp.service.implementation.SearchImplementation;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private SearchImplementation searchImplementation;

    private ScheduleDTO scheduleDTOObject(){
        return  new ScheduleDTO("Test Flight",
                "Fake Airlines",
                "MAA",
                "DEL",
                LocalDate.now(),
                LocalDateTime.now(),
                4311,
                120);
    }

    @Test
    void searchSchedule_throwsCityNotFound(){
        SearchQueryDTO search = new SearchQueryDTO("xyz","abc",LocalDate.now());

        when(cityRepository.findCityByAirportCode(search.fromCityAirportCode()))
                .thenReturn(Mono.just(new City()));
        when(cityRepository.findCityByAirportCode(search.toCityAirportCode()))
                .thenReturn(Mono.empty());

        StepVerifier.create(searchImplementation.searchFlight(search))
                .expectError(CityNotFoundException.class)
                .verify();
    }

    @Test
    void searchSchedule_success(){
        SearchQueryDTO search = new SearchQueryDTO("xyz","abc",LocalDate.now());

        when(cityRepository.findCityByAirportCode(search.fromCityAirportCode()))
                .thenReturn(Mono.just(new City()));
        when(cityRepository.findCityByAirportCode(search.toCityAirportCode()))
                .thenReturn(Mono.just(new City()));

        Schedule schedule = new Schedule();
        schedule.setFromCityAirportCode("xyz");
        schedule.setToCityAirportCode("abc");

        when(scheduleRepository
                .findScheduleByFromCityAirportCodeAndToCityAirportCodeAndDepartureDate("xyz", "abc", search.date()))
                .thenReturn(Flux.just(schedule));

        StepVerifier.create(searchImplementation.searchFlight(search))
                .expectNext(schedule)
                .verifyComplete();
    }
}