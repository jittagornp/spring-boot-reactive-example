/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import com.pamarin.learning.webflux.config.KongProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Slf4j
@RestController
public class HomeController {

    private final KongProperties properties;

    @Autowired
    public HomeController(KongProperties properties) {
        this.properties = properties;
    }

    @GetMapping({"", "/"})
    public Mono<KongProperties> hello() {
        return Mono.just(properties);
    }
}
