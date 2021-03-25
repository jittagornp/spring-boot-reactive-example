/*
 * Copyright 2017-2019 Pamarin.com
 */
package me.jittagornp.example.reactive.error.handler;

import me.jittagornp.example.reactive.error.ErrorResponse;
import me.jittagornp.example.reactive.error.ErrorResponseExceptionHandlerAdapter;
import me.jittagornp.example.reactive.exception.InvalidUsernamePasswordException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Component
public class ErrorResponseInvalidUsernamePasswordExceptionHandler extends ErrorResponseExceptionHandlerAdapter<InvalidUsernamePasswordException> {

    @Override
    public Class<InvalidUsernamePasswordException> getTypeClass() {
        return InvalidUsernamePasswordException.class;
    }

    @Override
    protected Mono<ErrorResponse> buildError(final ServerWebExchange exchange, final InvalidUsernamePasswordException e) {
        return Mono.fromCallable(() -> {
            return ErrorResponse.invalidRequest("Invalid username or password");
        });
    }
}
