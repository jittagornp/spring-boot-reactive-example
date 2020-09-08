/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@RestController
public class HomeController {


    @GetMapping({"", "/"})
    public Mono<String> hello() {
       throw new RuntimeException("Error on /");
    }

}
