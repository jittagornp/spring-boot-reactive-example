/*
 * Copyright 2017-2019 Pamarin.com
 */
package me.jittagornp.example.reactive;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Slf4j
public class MonoDefaultIfEmptyExample {

    public static void main(String[] args) {
        Mono.justOrEmpty(null)
                .defaultIfEmpty("Hello World")
                .doOnNext(message -> {
                    log.info("message => {}", message);
                })
                .subscribe();
    }

}
