/*
 * Copyright 2017-2019 Pamarin.com
 */
package me.jittagornp.example.reactive.handler;


import me.jittagornp.example.reactive.exception.AuthenticationException;
import me.jittagornp.example.reactive.model.ErrorResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Component
public class ErrorResponseAuthenticationExceptionHandler extends ErrorResponseExceptionHandlerAdapter<AuthenticationException> {

    @Override
    public Class<AuthenticationException> getTypeClass() {
        return AuthenticationException.class;
    }

    @Override
    protected Mono<ErrorResponse> buildError(final ServerWebExchange exchange, final AuthenticationException e) {
        return Mono.fromCallable(() -> {
            return ErrorResponse.unauthorized();
        });
    }
}
