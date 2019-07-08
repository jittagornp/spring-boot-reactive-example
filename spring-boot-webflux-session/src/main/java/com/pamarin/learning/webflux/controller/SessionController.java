/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Slf4j
@RestController
public class SessionController {

    @GetMapping({"", "/", "/session"})
    public Mono<String> statelessSession(WebSession webSession) {
        return Mono.just("stateless session => " + webSession.getId() + ", is started => " + webSession.isStarted());
    }

    @GetMapping("/session/new")
    public Mono<String> createSession(WebSession webSession) {
        webSession.start();
        return Mono.just("create session => " + webSession.getId());
    }
    
    @GetMapping("/session/invalidate")
    public Mono<String> invalidateSession(WebSession webSession) {
        webSession.invalidate();
        return Mono.just("invalidate session => " + webSession.getId());
    }
}
