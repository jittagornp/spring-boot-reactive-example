/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.handler;

import me.jittagornp.example.reactive.model.ErrorResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 * @param <E>
 */
public interface ErrorResponseExceptionHandler<E extends Throwable> {

    Class<E> getTypeClass();

    Mono<ErrorResponse> handle(final ServerWebExchange exchange, final E e);

}
