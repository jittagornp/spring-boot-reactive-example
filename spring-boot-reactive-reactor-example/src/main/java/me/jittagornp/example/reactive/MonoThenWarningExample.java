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
public class MonoThenWarningExample {

    public static void main(String[] args) {
        Mono<String> task1 = Mono.fromCallable(() -> {
            log.info("do task 1");
            return "task 1";
        });
        Mono<String> task2 = Mono.fromCallable(() -> {
            log.info("do task 2");
            return "task 2";
        });
        Mono<String> task3 = Mono.defer(() -> {
            log.info("do task 3");
            return Mono.error(new RuntimeException("something error"));
        });
        Mono<String> task4 = Mono.fromCallable(() -> {
            log.info("do task 4");
            return "task 4";
        });
        Mono<String> task5 = Mono.fromCallable(() -> {
            log.info("do task 5");
            return "task 5";
        });

        task1
                .doOnNext(value -> log.info("value => {}", value))
                .then(task2)
                .doOnNext(value -> log.info("value => {}", value))
                .then(task3)
                .doOnNext(value -> log.info("value => {}", value))
                .then(task4)
                .doOnNext(value -> log.info("value => {}", value))
                .then(task5)
                .subscribe();
    }

}
