/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Slf4j
@RestController
public class HomeController {

    @GetMapping({"", "/"})
    public Mono<String> hello() {
        log.debug("call hello method");
        return Mono.just("Hello world.");
    }
}
