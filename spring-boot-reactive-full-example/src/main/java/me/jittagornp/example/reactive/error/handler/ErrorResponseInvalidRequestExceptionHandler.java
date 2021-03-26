/*
 * Copyright 2017-2019 Pamarin.com
 */
package me.jittagornp.example.reactive.error.handler;

import me.jittagornp.example.reactive.error.ErrorResponse;
import me.jittagornp.example.reactive.error.ErrorResponseExceptionHandlerAdapter;
import me.jittagornp.example.reactive.exception.InvalidRequestException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Component
public class ErrorResponseInvalidRequestExceptionHandler extends ErrorResponseExceptionHandlerAdapter<InvalidRequestException> {

    @Override
    public Class<InvalidRequestException> getTypeClass() {
        return InvalidRequestException.class;
    }

    @Override
    protected Mono<ErrorResponse> buildError(final ServerWebExchange exchange, final InvalidRequestException e) {
        return Mono.fromCallable(() -> {
            return ErrorResponse.invalidRequest(e.getMessage());
        });
    }
}
