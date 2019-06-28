/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.handler;

import com.pamarin.learning.webflux.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseResponseStatusExceptionHandler extends ErrorResponseExceptionHandlerAdapter<ResponseStatusException> {

    @Override
    public Class<ResponseStatusException> getTypeClass() {
        return ResponseStatusException.class;
    }

    @Override
    protected ErrorResponse buildError(ServerWebExchange exchange, ResponseStatusException ex) {
        //400
        if (ex.getStatus() == HttpStatus.BAD_REQUEST) {
            return ErrorResponse.invalidRequest(ex.getMessage());
        }
        //401
        if (ex.getStatus() == HttpStatus.UNAUTHORIZED) {
            return ErrorResponse.unauthorized(ex.getMessage());
        }
        //403
        if (ex.getStatus() == HttpStatus.FORBIDDEN) {
            return ErrorResponse.accessDenied(ex.getMessage());
        }
        //404
        if (ex.getStatus() == HttpStatus.NOT_FOUND) {
            return ErrorResponse.notFound(ex.getMessage());
        }
        return ErrorResponse.serverError();
    }
}
