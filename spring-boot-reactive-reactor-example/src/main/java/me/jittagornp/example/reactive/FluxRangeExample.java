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
public class FluxRangeExample {
    
    public static void main(String[] args) {
        int start = 3;
        int count = 5;
        Flux.range(start, count)
                .doOnNext(message -> {
                    log.info("message => {}", message);
                })
                .subscribe();
    }
    
}
