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
public class MonoEmptyExample {

    public static void main(String[] args) {
        Mono.empty()
                .doOnNext(message -> {
                    log.info("message => {}", message);
                })
                .subscribe();
    }

}
