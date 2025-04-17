/*
 * Copyright 2017-2019 Pamarin.com
 */
package me.jittagornp.example.reactive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

/**
 *
 * @author jitta
 */
@Slf4j
public class MonoOnErrorResumeExample {

    private static class NotFoundException extends RuntimeException {

        public NotFoundException(String message) {
            super(message);
        }

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class User {

        private String username;
    }

    private static Mono<User> getUser() {
        return Mono.create((MonoSink<User> callbback) -> {
            final int randomNumber = (int) (Math.random() * 100);
            if (randomNumber % 2 == 0) {
                callbback.success(User.builder().username("jittagornp").build());
            } else {
                callbback.error(new NotFoundException("Not found user"));
            }
        });
    }

    public static void main(String[] args) {
        getUser()
                .onErrorResume(NotFoundException.class, e -> {
                    log.info("error => {}", e.getMessage());
                    return Mono.just(User.builder().username("anonymous").build());
                })
                .doOnNext(user -> {
                    log.info("username => {}", user.getUsername());
                })
                .subscribe();
    }

}
