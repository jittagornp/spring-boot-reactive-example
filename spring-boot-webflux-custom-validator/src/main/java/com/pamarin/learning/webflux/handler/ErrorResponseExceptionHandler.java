/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.handler;

import com.pamarin.learning.webflux.model.ErrorResponse;
import org.springframework.web.server.ServerWebExchange;

/**
 *
 * @author jitta
 * @param <E>
 */
public interface ErrorResponseExceptionHandler<E extends Throwable> {

    Class<E> getTypeClass();

    ErrorResponse handle(ServerWebExchange exchange, E e);

}
