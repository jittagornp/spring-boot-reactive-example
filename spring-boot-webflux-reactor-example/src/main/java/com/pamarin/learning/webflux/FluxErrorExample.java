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
public class FluxErrorExample {
    
    public static void main(String[] args) {
        Flux.empty()
                .switchIfEmpty(Flux.error(new RuntimeException("Not found data")))
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .doOnError(e -> {
                    log.debug("error => {}", e.getMessage());
                })
                .subscribe();
    }
    
}
