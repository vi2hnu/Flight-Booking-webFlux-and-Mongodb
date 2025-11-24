package org.example.flightapp.controller;

import org.example.flightapp.DTO.SearchQueryDTO;
import org.example.flightapp.model.entity.Schedule;
import org.example.flightapp.service.SearchInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import static org.mockito.Mockito.when;

@WebFluxTest(SearchController.class)
public class SearchControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private SearchInterface searchInterface;

    @Test
    public void searchFlight() {
        SearchQueryDTO search = new SearchQueryDTO("cityA","cityB", LocalDate.now());

        Schedule schedule = new Schedule();
        schedule.setId("123");

        when(searchInterface.searchFlight(search)).thenReturn(Flux.just(schedule));

        webTestClient.post()
                .uri("/api/v1.0/flight/search")
                .bodyValue(search)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Schedule.class)
                .hasSize(1)
                .contains(schedule);
    }

}
