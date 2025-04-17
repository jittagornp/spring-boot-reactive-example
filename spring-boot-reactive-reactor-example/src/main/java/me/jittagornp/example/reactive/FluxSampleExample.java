/*
 * Copyright 2017-2019 Pamarin.com
 */
package me.jittagornp.example.reactive;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 *
 * @author jitta
 */
@Slf4j
public class FluxSampleExample {

    public static void main(String[] args) {
        Flux.create(callback -> {
            for (int i = 0; i < 15; i++) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException ex) {

                }
                callback.next(i);
            }
            callback.complete();

        })
                .sample(Duration.ofMillis(300))
                .doOnNext(message -> {
                    log.info("message => {}", message);
                })
                .subscribe();
    }

}
