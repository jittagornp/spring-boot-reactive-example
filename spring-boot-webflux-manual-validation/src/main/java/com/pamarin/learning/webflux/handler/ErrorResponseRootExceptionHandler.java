/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.handler;

import com.pamarin.learning.webflux.model.ErrorResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseRootExceptionHandler extends ErrorResponseExceptionHandlerAdapter<Exception> {

    @Override
    public Class<Exception> getTypeClass() {
        return Exception.class;
    }

    @Override
    protected ErrorResponse buildError(ServerWebExchange exchange, Exception ex) {
        return ErrorResponse.serverError();
    }
}
