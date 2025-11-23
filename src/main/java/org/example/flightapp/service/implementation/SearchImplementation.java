package org.example.flightapp.service.implementation;

import org.example.flightapp.DTO.SearchQueryDTO;
import org.example.flightapp.model.entity.Schedule;
import org.example.flightapp.repository.ScheduleRepository;
import org.example.flightapp.service.SearchInterface;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SearchImplementation implements SearchInterface {

    private final ScheduleRepository scheduleRepository;
    public SearchImplementation(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Flux<Schedule> searchFlight(SearchQueryDTO searchQueryDTO) {
        return scheduleRepository.findScheduleByFromCityNameAndToCityNameAndDepartureDate(
                searchQueryDTO.fromCity()
                ,searchQueryDTO.toCity()
                ,searchQueryDTO.date());
    }
}
