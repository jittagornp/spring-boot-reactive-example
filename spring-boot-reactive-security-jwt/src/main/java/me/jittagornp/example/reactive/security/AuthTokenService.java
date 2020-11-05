/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.security;

import java.util.Map;

import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
public interface AuthTokenService {

    Mono<String> sign(final Map<String, Object> claims);

    Mono<Map<String, Object>> verify(final String token);

}
