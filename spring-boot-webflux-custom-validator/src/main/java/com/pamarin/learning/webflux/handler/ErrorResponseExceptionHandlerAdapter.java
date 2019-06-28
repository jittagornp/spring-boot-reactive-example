/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.handler;

import com.pamarin.learning.webflux.model.ErrorResponse;
import java.util.UUID;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

/**
 *
 * @author jitta
 * @param <E>
 */
public abstract class ErrorResponseExceptionHandlerAdapter<E extends Throwable> implements ErrorResponseExceptionHandler<E> {

    protected abstract ErrorResponse buildError(ServerWebExchange exchange, E e);

    private String getErrorCode(ServerWebExchange exchange) {
        return UUID.randomUUID().toString();
    }

    private ErrorResponse additional(ErrorResponse err, ServerWebExchange exchange, E e) {
        ServerHttpRequest httpReq = exchange.getRequest();
        err.setState(httpReq.getQueryParams().getFirst("state"));
        err.setErrorTimestamp(System.currentTimeMillis());
        err.setErrorCode(getErrorCode(exchange));
        return err;
    }

    @Override
    public ErrorResponse handle(ServerWebExchange exchange, E e) {
        ErrorResponse err = buildError(exchange, e);
        return additional(err, exchange, e);
    }

}
