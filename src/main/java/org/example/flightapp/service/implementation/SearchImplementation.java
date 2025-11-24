package org.example.flightapp.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.flightapp.DTO.SearchQueryDTO;
import org.example.flightapp.exception.CityNotFoundException;
import org.example.flightapp.model.entity.Schedule;
import org.example.flightapp.repository.CityRepository;
import org.example.flightapp.repository.ScheduleRepository;
import org.example.flightapp.service.SearchInterface;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class SearchImplementation implements SearchInterface {

    private final ScheduleRepository scheduleRepository;
    private final CityRepository cityRepository;

    public SearchImplementation(ScheduleRepository scheduleRepository, CityRepository cityRepository) {
        this.scheduleRepository = scheduleRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public Flux<Schedule> searchFlight(SearchQueryDTO searchQueryDTO) {
        String cityNotFoundError = "City Not Found: ";
        //validating the both the cities first
        Mono<Void> validation = Mono.zip(
                        cityRepository.findCityByAirportCode(searchQueryDTO.fromCityAirportCode())
                                .doOnNext(city->log.error(cityNotFoundError + searchQueryDTO.fromCityAirportCode()))
                                .switchIfEmpty(Mono.error(new CityNotFoundException(cityNotFoundError + searchQueryDTO.fromCityAirportCode()))),
                        cityRepository.findCityByAirportCode(searchQueryDTO.toCityAirportCode())
                                .doOnNext(city->log.error(cityNotFoundError + searchQueryDTO.toCityAirportCode()))
                                .switchIfEmpty(Mono.error(new CityNotFoundException(cityNotFoundError + searchQueryDTO.toCityAirportCode())))
                )
                .then();

        return validation.thenMany(
                scheduleRepository.findScheduleByFromCityAirportCodeAndToCityAirportCodeAndDepartureDate(
                        searchQueryDTO.fromCityAirportCode(),
                        searchQueryDTO.toCityAirportCode(),
                        searchQueryDTO.date()
                )
        );
    }
}
