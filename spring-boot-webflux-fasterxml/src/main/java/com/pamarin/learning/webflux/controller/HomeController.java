/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@RestController
public class HomeController {

    @GetMapping({"", "/"})
    public Mono<String> hello() {
        return Mono.just("Hello world.");
    }
}
