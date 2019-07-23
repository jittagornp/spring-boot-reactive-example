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
public class MonoThenExample {
    
    private static Mono<String> task(final String message, int delay) {
        return Mono.defer(() -> {
            try {
                Thread.sleep(delay * 1000);
            } catch (InterruptedException ex) {

            }
            return Mono.just(message + " " + LocalDateTime.now());
        });
    }

    public static void main(String[] args) {

        Mono<String> task1 = task("Hello 1 at", 3);
        Mono<String> task2 = task("Hello 2 at", 1);
        Mono<String> task3 = task("Hello 3 at", 2);
        
        task1
                .doOnNext(message -> {
                    log.debug("message 1 => {}", message);
                })
                .then(task2)
                .doOnNext(message -> {
                    log.debug("message 2 => {}", message);
                })
                .then(task3)
                .doOnNext(message -> {
                    log.debug("message 3 => {}", message);
                })
                .subscribe();
    }
    
}
