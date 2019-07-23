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
public class MonoBlockExample {
    
    public static void main(String[] args) {
        String message = Mono.just("Hello World").block();
        log.debug("message => {}", message);
    }
    
}
