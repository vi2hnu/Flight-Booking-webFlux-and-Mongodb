package org.example.flightapp.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.flightapp.DTO.SearchQueryDTO;
import org.example.flightapp.exception.CityNotFoundException;
import org.example.flightapp.model.entity.City;
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
        Mono<Void> validation = Mono.zip(
                        cityRepository.findCityByCityName(searchQueryDTO.fromCity())
                                .doOnNext(city->log.error("City not found"))
                                .switchIfEmpty(Mono.error(new CityNotFoundException("City not found: " + searchQueryDTO.fromCity()))),
                        cityRepository.findCityByCityName(searchQueryDTO.toCity())
                                .doOnNext(city->log.error("City not found"))
                                .switchIfEmpty(Mono.error(new CityNotFoundException("City not found: " + searchQueryDTO.toCity())))
                )
                .then();

        return validation.thenMany(
                scheduleRepository.findScheduleByFromCityNameAndToCityNameAndDepartureDate(
                        searchQueryDTO.fromCity(),
                        searchQueryDTO.toCity(),
                        searchQueryDTO.date()
                )
        );
    }
}
