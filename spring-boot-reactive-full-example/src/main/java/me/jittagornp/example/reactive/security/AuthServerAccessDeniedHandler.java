/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Slf4j
public class AuthServerAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final org.springframework.security.access.AccessDeniedException e) {
        if (isAuthenticated(exchange)) {
            return Mono.error(new AccessDeniedException("Access denied"));
        }
        return Mono.error(new AuthenticationException("Please login"));
    }

    private boolean isAuthenticated(final ServerWebExchange exchange) {
        return exchange.getAttribute("SPRING_SECURITY_CONTEXT") != null;
    }
}
