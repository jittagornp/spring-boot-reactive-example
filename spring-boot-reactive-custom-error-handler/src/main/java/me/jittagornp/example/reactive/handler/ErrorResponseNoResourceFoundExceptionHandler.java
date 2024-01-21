/*
 * Copyright 2017-2019 Pamarin.com
 */
package me.jittagornp.example.reactive.handler;


import me.jittagornp.example.reactive.model.ErrorResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Component
public class ErrorResponseNoResourceFoundExceptionHandler extends ErrorResponseExceptionHandlerAdapter<NoResourceFoundException> {

    @Override
    public Class<NoResourceFoundException> getTypeClass() {
        return NoResourceFoundException.class;
    }

    @Override
    protected Mono<ErrorResponse> buildError(final ServerWebExchange exchange, final NoResourceFoundException e) {
        return Mono.fromCallable(() -> {
            return ErrorResponse.notFound();
        });
    }
}
