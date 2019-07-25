/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux;

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
                    log.debug("size => {}", map.size());
                    log.debug("map => {}", map);
                    log.debug("day => {}", map.get("Sun"));
                    log.debug("day => {}", map.get("Mon"));
                    log.debug("day => {}", map.get("Tue"));
                    log.debug("day => {}", map.get("Wed"));
                    log.debug("day => {}", map.get("Thu"));
                    log.debug("day => {}", map.get("Fri"));
                    log.debug("day => {}", map.get("Sat"));
                })
                .subscribe();
    }

}
