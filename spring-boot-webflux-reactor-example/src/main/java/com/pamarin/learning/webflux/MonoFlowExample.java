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
public class MonoFlowExample {

    public static void main(String[] args) {
        Mono.just("A")
                .doFirst(() -> log.debug("doFirst..."))
                .doOnRequest(value -> log.debug("doOnRequest... {}", value))
                .doOnEach(signal -> log.debug("doOnEach... {} : value => {}", signal.getType().name(), signal.get()))
                .doOnNext(value -> log.debug("doOnNext... {}", value))
                .doOnCancel(() -> log.debug("doOnCacel..."))
                .doOnError(e -> log.debug("doOnError... {}", e.getMessage()))
                .doOnSuccess(value -> log.debug("doOnSuccess... {}", value))
                .doOnSuccessOrError((value, e) -> log.debug("doOnSuccessOrError... {} or {}", value, (e == null ? null : e.getMessage())))
                .doAfterSuccessOrError((value, e) -> log.debug("doAfterSuccessOrError... {} or {}", value, (e == null ? null : e.getMessage())))
                .doAfterTerminate(() -> log.debug("doAfterTerminate..."))
                .doOnTerminate(() -> log.debug("doOnTerminate..."))
                .doOnSubscribe(subscription -> {
                    long id = 1234567890;
                    subscription.request(id);
                    log.debug("doOnSubscribe... {}", id);
                })
                .doFinally(signalType -> log.debug("doFinally... {}", signalType.toString()))
                .doOnDiscard(Object.class, value -> log.debug("doOnDiscard... {}", value))
                .subscribe();
    }

}
