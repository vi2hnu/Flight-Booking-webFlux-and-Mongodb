package org.example.flightapp.controller;

import org.example.flightapp.DTO.SearchQueryDTO;
import org.example.flightapp.model.entity.Schedule;
import org.example.flightapp.service.SearchInterface;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1.0/flight/search")
public class SearchController {

    private final SearchInterface searchInterface;
    public SearchController(SearchInterface searchInterface) {
        this.searchInterface = searchInterface;
    }

    @PostMapping("")
    public Flux<Schedule> searchFlight(@RequestBody SearchQueryDTO searchQueryDTO) {
        return searchInterface.searchFlight(searchQueryDTO);
    }

}
