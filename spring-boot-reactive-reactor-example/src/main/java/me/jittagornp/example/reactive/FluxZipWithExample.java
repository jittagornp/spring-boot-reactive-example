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
public class FluxZipWithExample {
    
    public static void main(String[] args) {
        Flux.just("1", "2", "3")
                .zipWith(Flux.just("4", "5", "6", "7", "8"))
                .doOnNext(message -> {
                    log.info("message => {}", message);
                })
                .subscribe();
    }
    
}
