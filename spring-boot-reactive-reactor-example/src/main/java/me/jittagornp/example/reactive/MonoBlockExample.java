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
public class MonoBlockExample {
    
    public static void main(String[] args) {
        String message = Mono.just("Hello World").block();
        log.info("message => {}", message);
    }
    
}
