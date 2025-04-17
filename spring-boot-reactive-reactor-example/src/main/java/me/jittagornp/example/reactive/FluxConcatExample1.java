/*
 * Copyright 2017-2019 Pamarin.com
 */
package me.jittagornp.example.reactive;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Slf4j
public class FluxConcatExample1 {
    
    public static void main(String[] args) {
        Flux.concat(
                Mono.just("task 1"),
                Mono.just("task 2"),
                Mono.just("task 3"),
                Mono.just("task 4"),
                Mono.just("task 5")
        )
                .doOnNext(message -> {
                    log.info("message => {}", message);
                })
                .subscribe();
    }
    
}
