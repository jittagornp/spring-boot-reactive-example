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
public class FluxCountExample {
    
    public static void main(String[] args) {
        Flux.just("1", "2", "3", "4", "5")
                .count()
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }
    
}
