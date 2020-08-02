/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Slf4j
@RestController
public class SessionController {

    @GetMapping({"", "/", "/session"})
    public Mono<WebSession> getSession(final WebSession webSession) {
        return Mono.just(webSession);
    }

    @GetMapping("/session/create")
    public Mono<String> createSession(final WebSession webSession) {
        webSession.start();
        return Mono.just("create session => " + webSession.getId());
    }

    @GetMapping("/session/invalidate")
    public Mono<String> invalidateSession(final WebSession webSession) {
        return webSession.invalidate()
                .then(Mono.just("invalidate session => " + webSession.getId()));
    }
}
