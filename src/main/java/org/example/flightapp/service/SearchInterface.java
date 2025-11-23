package org.example.flightapp.service;

import org.example.flightapp.DTO.SearchQueryDTO;
import org.example.flightapp.model.entity.Schedule;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;

public interface SearchInterface {
    Flux<Schedule> searchFlight(SearchQueryDTO searchQueryDTO);
}
