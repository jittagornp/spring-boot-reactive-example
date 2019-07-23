/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Slf4j
public class MonoTimeoutExample {

    public static void main(String[] args) {
        Mono.create(callback -> {
            try {
                log.debug("wait 5 seconds... at " + LocalDateTime.now());
                Thread.sleep(5000L);
            } catch (InterruptedException ex) {
                //
            }
            callback.success("Hello at " + LocalDateTime.now());
        })
                .timeout(Duration.ofSeconds(3))
                .onErrorResume(TimeoutException.class, e -> Mono.just("Hello from timeout"))
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
