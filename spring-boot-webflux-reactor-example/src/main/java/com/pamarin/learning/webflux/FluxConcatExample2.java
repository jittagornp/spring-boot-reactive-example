/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Slf4j
public class FluxConcatExample2 {
    
    public static void main(String[] args) {
        Flux.concat(
                Mono.just("1"),
                Flux.just("2", "3"),
                Mono.just("4"),
                Flux.just("5", "6", "7"),
                Mono.just("8")
        )
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }
    
}
