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
    public SearchController(SearchInterface searchInterface, CityRepository cityRepository) {
        this.searchInterface = searchInterface;
    }

    @PostMapping("")
    public Flux<Schedule> searchFlight(@RequestBody SearchQueryDTO searchQueryDTO) {
        return searchInterface.searchFlight(searchQueryDTO);
    }

}
