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
public class MonoCreateExample {
 
     public static void main(String[] args) {
        Mono.create(callback -> {
            try {
                log.debug("wait 3 seconds... at " + LocalDateTime.now());
                Thread.sleep(3000L);
            } catch (InterruptedException ex) {
                //
            }
            callback.success("Hello at " + LocalDateTime.now());
        })
        .doOnNext(message -> {
            log.debug("message => {}", message);
        })
        .subscribe();
    }
    
}
