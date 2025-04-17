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
public class FluxBufferExample {

    public static void main(String[] args) {
        Flux.create(callback -> {
            for (int i = 0; i < 15; i++) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ex) {

                }
                callback.next(i);
            }
            callback.complete();

        })
                .buffer(5)
                .doOnNext(message -> {
                    log.info("message => {}", message);
                })
                .subscribe();
    }

}
