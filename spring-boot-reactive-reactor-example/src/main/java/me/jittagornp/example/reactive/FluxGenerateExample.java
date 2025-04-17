/*
 * Copyright 2017-2019 Pamarin.com
 */
package me.jittagornp.example.reactive;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 *
 * @author jitta
 */
@Slf4j
public class FluxGenerateExample {

    public static void main(String[] args) {
        Flux.generate(
                () -> 0, //initial value or state 
                (value, sink) -> {

                    if (value >= 5) {
                        sink.complete();
                    }

                    sink.next(value + " at " + LocalDateTime.now());

                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException ex) {

                    }

                    return value + 1;
                })
                .doOnNext(message -> {
                    log.info("message => {}", message);
                })
                .subscribe();
    }

}
