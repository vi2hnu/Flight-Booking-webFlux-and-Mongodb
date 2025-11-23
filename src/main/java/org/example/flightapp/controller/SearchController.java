package org.example.flightapp.controller;

import org.example.flightapp.DTO.SearchQueryDTO;
import org.example.flightapp.model.entity.City;
import org.example.flightapp.model.entity.Schedule;
import org.example.flightapp.repository.CityRepository;
import org.example.flightapp.service.SearchInterface;
import org.reactivestreams.Publisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1.0/flight/search")
public class SearchController {

    private final SearchInterface searchInterface;
    private final CityRepository cityRepository;
    public SearchController(SearchInterface searchInterface, CityRepository cityRepository) {
        this.searchInterface = searchInterface;
        this.cityRepository = cityRepository;
    }

    @PostMapping("")
    public Flux<Schedule> searchFlight(@RequestBody SearchQueryDTO searchQueryDTO) {
        return searchInterface.searchFlight(searchQueryDTO);
//        return schedules.hasElements()
//                .flatMap(hasElements -> {
//                    if (hasElements) {
//                        return Mono.just(ResponseEntity.ok(schedules));
//                    } else {
//                        return Mono.just(ResponseEntity.notFound().build());
//                    }
//                });
    }

    @GetMapping("/{city}")
    public Publisher<City> searchFlightByCity(@PathVariable String city) {
        return cityRepository.findCityByAirportCode(city);
    }
}
