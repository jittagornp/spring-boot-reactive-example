/*
 * Copyright 2017-2019 Pamarin.com
 */
package me.jittagornp.example.reactive;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 *
 * @author jitta
 */
@Slf4j
public class FluxMapExample {
    
    public static void main(String[] args) {
        Flux.just(1, 2, 3, 4, 5)
                .map(number -> number * 2)
                .doOnNext(message -> {
                    log.info("message => {}", message);
                })
                .subscribe();
    }
    
}
