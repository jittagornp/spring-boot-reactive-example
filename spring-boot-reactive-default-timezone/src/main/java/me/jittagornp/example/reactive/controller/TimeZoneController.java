package me.jittagornp.example.reactive.controller;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.*;
import java.util.Arrays;
import java.util.TimeZone;

@RestController
@RequestMapping("/time-zones")
public class TimeZoneController {

    @GetMapping
    public Flux<TimeZone> getAvailableTimeZones(){
        return Flux.fromIterable(Arrays.asList(TimeZone.getAvailableIDs()))
                .map(TimeZone::getTimeZone);
    }

    @GetMapping("/default")
    public Mono<TimeZone> getDefault() {
        return Mono.just(TimeZone.getDefault());
    }

    @GetMapping("/current-datetime")
    public Mono<DTO> currentDateTime(){
        return Mono.just(
                DTO.builder()
                        .instant(Instant.now())
                        .localDate(LocalDate.now())
                        .localDateTime(LocalDateTime.now())
                        .offsetDateTime(OffsetDateTime.now())
                        .offsetTime(OffsetTime.now())
                        .zonedDateTime(ZonedDateTime.now())
                        .build()
        );
    }

    @Data
    @Builder
    public static class DTO {

        private Instant instant;

        private LocalDate localDate;

        private LocalDateTime localDateTime;

        private OffsetDateTime offsetDateTime;

        private OffsetTime offsetTime;

        private ZonedDateTime zonedDateTime;

    }

}
