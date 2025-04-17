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
public class MonoFlowExample {

    public static void main(String[] args) {
        Mono.just("A")
                .doFirst(() -> log.info("doFirst..."))
                .doOnRequest(value -> log.info("doOnRequest... {}", value))
                .doOnEach(signal -> log.info("doOnEach... {} : value => {}", signal.getType().name(), signal.get()))
                .doOnNext(value -> log.info("doOnNext... {}", value))
                .doOnCancel(() -> log.info("doOnCacel..."))
                .doOnError(e -> log.info("doOnError... {}", e.getMessage()))
                .doOnSuccess(value -> log.info("doOnSuccess... {}", value))
                //.doOnSuccessOrError((value, e) -> log.info("doOnSuccessOrError... {} or {}", value, (e == null ? null : e.getMessage())))
                //.doAfterSuccessOrError((value, e) -> log.info("doAfterSuccessOrError... {} or {}", value, (e == null ? null : e.getMessage())))
                .doAfterTerminate(() -> log.info("doAfterTerminate..."))
                .doOnTerminate(() -> log.info("doOnTerminate..."))
                .doOnSubscribe(subscription -> {
                    long id = 1234567890;
                    subscription.request(id);
                    log.info("doOnSubscribe... {}", id);
                })
                .doFinally(signalType -> log.info("doFinally... {}", signalType.toString()))
                .doOnDiscard(Object.class, value -> log.info("doOnDiscard... {}", value))
                .subscribe();
    }

}
