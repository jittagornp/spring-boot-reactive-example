/*
 * Copyright 2017-2019 Pamarin.com
 */
package me.jittagornp.example.reactive;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 *
 * @author jitta
 */
@Slf4j
public class FluxSwitchIfEmptyExample {
    
    public static void main(String[] args) {
        Flux.fromIterable(Collections.emptyList())
                .switchIfEmpty(Flux.just("1", "2", "3"))
                .doOnNext(message -> {
                    log.info("message => {}", message);
                })
                .subscribe();
    }
    
}
