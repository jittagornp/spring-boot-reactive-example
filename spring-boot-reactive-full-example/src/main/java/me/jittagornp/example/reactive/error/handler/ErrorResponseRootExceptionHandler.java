/*
 * Copyright 2017-2019 Pamarin.com
 */
package me.jittagornp.example.reactive.error.handler;

import me.jittagornp.example.reactive.error.ErrorResponse;
import me.jittagornp.example.reactive.error.ErrorResponseExceptionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Component
public class ErrorResponseRootExceptionHandler extends ErrorResponseExceptionHandlerAdapter<Exception> {

    @Override
    public Class<Exception> getTypeClass() {
        return Exception.class;
    }

    @Override
    protected Mono<ErrorResponse> buildError(final ServerWebExchange exchange, final Exception e) {
        return Mono.fromCallable(() -> {
            return ErrorResponse.serverError();
        });
    }
}
