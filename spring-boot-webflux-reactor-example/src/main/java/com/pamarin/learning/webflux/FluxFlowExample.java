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
public class FluxFlowExample {

    public static void main(String[] args) {
        Flux.just("1", "2", "3")
                .doFirst(() -> {
                    log.debug("doFirst...");
                })
                .doOnRequest(value -> {
                    log.debug("doOnRequest... {}", value);
                })
                .doOnEach(consumer -> {
                    log.debug("doOnEach... {} : value => {}", consumer.getType().name(), consumer.get());
                })
                .doOnNext(value -> {
                    log.debug("doOnNext... {}", value);
                })
                .doOnCancel(() -> {
                    log.debug("doOnCacel...");
                })
                .doOnError(e -> {
                    log.debug("doOnError... {}", e.getMessage());
                })
                .doOnComplete(() -> {
                    log.debug("doOnComplete...");
                })
                .doAfterTerminate(() -> {
                    log.debug("doAfterTerminate...");
                })
                .doOnTerminate(() -> {
                    log.debug("doOnTerminate...");
                })
                .doOnSubscribe(consumer -> {
                    consumer.request(111);
                    log.debug("doOnSubscribe...");
                })
                .doFinally(consumer -> {
                    log.debug("doFinally... {}", consumer.toString());
                })
                .doOnDiscard(Object.class, consumer -> {
                    log.debug("doOnDiscard... {}", consumer.toString());
                })
                .subscribe();
    }

}
