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
public class MonoVoidWarningExample {

    private static Mono<Void> doSomething() {
        return Mono.fromRunnable(() -> {
            log.info("do something...");
        });
    }

    public static void main(String[] args) {
        doSomething()
                .flatMap(value -> {
                    log.info("flatMap :: value => {}", value);
                    return Mono.just(value);
                })
                .map(value -> {
                    log.info("map :: value => {}", value);
                    return value;
                })
                .doOnNext(value -> {
                    log.info("doOnNext :: value => {}", value);
                })
                .then(Mono.fromRunnable(() -> {
                    log.info("then do something ...");
                }))
                .doOnSuccess(value -> {
                    log.info("doOnSuccess :: value => {}", value);
                })
                .subscribe();
    }

}
