package me.jittagornp.example.reactive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

}
