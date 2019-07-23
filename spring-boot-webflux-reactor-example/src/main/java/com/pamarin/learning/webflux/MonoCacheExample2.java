/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Slf4j
public class MonoCacheExample2 {
    
     public static void main(String[] args) {
        Mono<String> defer = Mono.defer(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {

            }
            return Mono.just("Hello at " + LocalDateTime.now());
        }).cache(Duration.ofMinutes(5));
        
        log.debug("message => {}", defer.block());
        log.debug("message => {}", defer.block());
        log.debug("message => {}", defer.block());
    }
    
}
