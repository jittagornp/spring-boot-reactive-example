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
public class FluxZipExample1 {
    
    public static void main(String[] args) {
        
        Flux<String> flux1 = Flux.just("1", "2", "3", "4", "5");
        Flux<String> flux2 = Flux.just("6", "7", "8");
        
        Flux.zip(flux1, flux2)
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }
    
}
