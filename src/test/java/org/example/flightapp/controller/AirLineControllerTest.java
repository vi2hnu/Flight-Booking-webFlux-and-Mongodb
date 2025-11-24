package org.example.flightapp.controller;

import org.example.flightapp.DTO.ScheduleDTO;
import org.example.flightapp.model.entity.Schedule;
import org.example.flightapp.service.AirLineInterface;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebFluxTest(AirLineController.class)
public class AirLineControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AirLineInterface airLineInterface;

    @Test
    void addSchedule_success() {
        ScheduleDTO dto = new ScheduleDTO(
                "FlightX",
                "Air India",
                "CityA",
                "CityB",
                LocalDate.now(),
                LocalDateTime.now(),
                120,
                180
        );

        Schedule schedule = new Schedule();
        schedule.setId("123");

       when(airLineInterface.addSchedule(dto))
                .thenReturn(Mono.just(schedule));

        webTestClient.post()
                .uri("/api/v1.0/flight/airline/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Schedule.class)
                .isEqualTo(schedule);
    }
}
