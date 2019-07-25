/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 *
 * @author jitta
 */
@Slf4j
public class FluxGroupByExample {

    public static void main(String[] args) {
        Flux.just("A", "A", "B", "C", "D", "B", "F", "C", "A")
                .groupBy(value -> value)
                .flatMap(flux -> flux.count().map(count -> flux.key() + ":" + count))
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
