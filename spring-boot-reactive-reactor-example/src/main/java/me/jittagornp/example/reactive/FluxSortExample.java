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
public class FluxSortExample {

    public static void main(String[] args) {
        Flux.just(2, 3, 5, 4, 1, 9, 7, 6, 8, 0)
                .doOnNext(message -> {
                    log.info("before sort => {}", message);
                })
                .sort((numb1, numb2) -> numb1 - numb2)
                .doOnNext(message -> {
                    log.info("sorted => {}", message);
                })
                .subscribe();
    }

}
