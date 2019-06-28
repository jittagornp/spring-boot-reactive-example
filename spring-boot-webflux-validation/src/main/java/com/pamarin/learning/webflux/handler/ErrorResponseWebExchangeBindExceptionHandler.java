/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.handler;

import com.pamarin.learning.webflux.model.ErrorResponse;
import static java.util.stream.Collectors.toList;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

/**
 *
 * @author jitta
 */
@Component
public class ErrorResponseWebExchangeBindExceptionHandler extends ErrorResponseExceptionHandlerAdapter<WebExchangeBindException> {

    @Override
    public Class<WebExchangeBindException> getTypeClass() {
        return WebExchangeBindException.class;
    }

    @Override
    protected ErrorResponse buildError(ServerWebExchange exchange, WebExchangeBindException ex) {
        return ErrorResponse.builder()
                .error("bad_request")
                .errorDescription("Validate fail")
                .errorStatus(HttpStatus.BAD_REQUEST.value())
                .errorFields(
                        ex.getFieldErrors()
                                .stream()
                                .map(f -> {
                                    return ErrorResponse.Field.builder()
                                            .name(f.getField())
                                            .code(f.getCode())
                                            .description(f.getDefaultMessage())
                                            .build();
                                })
                                .collect(toList())
                )
                .build();
    }
}
