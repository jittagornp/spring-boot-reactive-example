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
public class FluxZipExample3 {

    private static Flux<String> create(int start, int to, long delayMillsec) {
        return Flux.create(callback -> {
            for (int i = start; i <= to; i++) {
                try {
                    Thread.sleep(delayMillsec);
                } catch (InterruptedException ex) {
                   
                }
                callback.next("" + i);
            }
            callback.complete();

        });
    }

    public static void main(String[] args) {

        Flux<String> flux1 = create(1, 5, 1000L);
        Flux<String> flux2 = create(6, 8, 3000L);
        Flux<String> flux3 = create(9, 12, 2000L);

        Flux.zip(flux1, flux2, flux3)
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
