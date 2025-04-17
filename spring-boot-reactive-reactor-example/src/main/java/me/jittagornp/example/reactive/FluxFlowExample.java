/*
 * Copyright 2017-2019 Pamarin.com
 */
package me.jittagornp.example.reactive;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 *
 * @author jitta
 */
@Slf4j
public class FluxFlowExample {

    public static void main(String[] args) {
        Flux.just("A", "B", "C")
                .doFirst(() -> log.info("doFirst..."))
                .doOnRequest(value -> log.info("doOnRequest... {}", value))
                .doOnEach(signal -> log.info("doOnEach... {} : value => {}", signal.getType().name(), signal.get()))
                .doOnNext(value -> log.info("doOnNext... {}", value))
                .doOnCancel(() -> log.info("doOnCacel..."))
                .doOnError(e -> log.info("doOnError... {}", e.getMessage()))
                .doOnComplete(() -> log.info("doOnComplete..."))
                .doAfterTerminate(() -> log.info("doAfterTerminate..."))
                .doOnTerminate(() -> log.info("doOnTerminate..."))
                .doOnSubscribe(subscription -> {
                    long id = 123456890;
                    subscription.request(id);
                    log.info("doOnSubscribe... {}", id);
                })
                .doFinally(signalType -> log.info("doFinally... {}", signalType.toString()))
                .doOnDiscard(Object.class, value -> log.info("doOnDiscard... {}", value))
                .subscribe();
    }

}
