/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Slf4j
public class MonoVoidWarningExample {

    private static Mono<Void> doSomething() {
        return Mono.fromRunnable(() -> {
            log.debug("do something...");
        });
    }

    public static void main(String[] args) {
        doSomething()
                .flatMap(value -> {
                    log.debug("flatMap :: value => {}", value);
                    return Mono.just(value);
                })
                .map(value -> {
                    log.debug("map :: value => {}", value);
                    return value;
                })
                .doOnNext(value -> {
                    log.debug("doOnNext :: value => {}", value);
                })
                .then(Mono.fromRunnable(() -> {
                    log.debug("then do something ...");
                }))
                .doOnSuccess(value -> {
                    log.debug("doOnSuccess :: value => {}", value);
                })
                .subscribe();
    }

}
