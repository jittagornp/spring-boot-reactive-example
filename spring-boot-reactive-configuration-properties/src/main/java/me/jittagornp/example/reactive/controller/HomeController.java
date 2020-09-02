/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import me.jittagornp.example.reactive.config.KongProperties;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {

    private final KongProperties properties;

    @GetMapping({"", "/"})
    public Mono<KongProperties> hello() {
        return Mono.just(properties);
    }
}
