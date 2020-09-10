/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.handler;


import me.jittagornp.example.reactive.model.ErrorResponse;
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
