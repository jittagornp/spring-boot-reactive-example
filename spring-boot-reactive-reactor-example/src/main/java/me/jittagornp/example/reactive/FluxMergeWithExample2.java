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
public class FluxMergeWithExample2 {
    
    public static void main(String[] args) {
        Flux<String> flux = Flux.just("1", "2", "3");
        Mono<String> mono = Mono.just("A");
        flux.mergeWith(mono)
                .doOnNext(message -> {
                    log.info("message => {}", message);
                })
                .subscribe();
    }
    
}
