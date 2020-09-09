/*
 * Copyright 2017-2019 Pamarin.com
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
