/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Slf4j
public class MonoCacheExample1 {
    
     public static void main(String[] args) {
        Mono<String> defer = Mono.defer(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {

            }
            return Mono.just("Hello at " + LocalDateTime.now());
        });
        
        log.debug("message => {}", defer.block());
        log.debug("message => {}", defer.block());
        log.debug("message => {}", defer.block());
    }

    
}
