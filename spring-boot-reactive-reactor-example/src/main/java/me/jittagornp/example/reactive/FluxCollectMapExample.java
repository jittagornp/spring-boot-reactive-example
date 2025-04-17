/*
 * Copyright 2017-2019 Pamarin.com
 */
package me.jittagornp.example.reactive;

import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 *
 * @author jitta
 */
@Slf4j
public class FluxCollectMapExample {

    public static void main(String[] args) {
        Flux.just(
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday"
        )
                .collectMap(value -> value.substring(0, 3))
                .doOnNext(map -> {
                    log.info("size => {}", map.size());
                    log.info("map => {}", map);
                    log.info("day => {}", map.get("Sun"));
                    log.info("day => {}", map.get("Mon"));
                    log.info("day => {}", map.get("Tue"));
                    log.info("day => {}", map.get("Wed"));
                    log.info("day => {}", map.get("Thu"));
                    log.info("day => {}", map.get("Fri"));
                    log.info("day => {}", map.get("Sat"));
                })
                .subscribe();
    }

}
