/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import com.pamarin.learning.webflux.model.ErrorResponse;
import java.util.UUID;
import static java.util.stream.Collectors.toList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> validationError(WebExchangeBindException ex, ServerWebExchange exchange) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(
                                ErrorResponse.builder()
                                        .error("bad_request")
                                        .errorDescription("Validate fail")
                                        .errorStatus(HttpStatus.BAD_REQUEST.value())
                                        .errorTimestamp(System.currentTimeMillis())
                                        .errorUri("https://developer.pamarin.com/document/error/")
                                        .errorCode(UUID.randomUUID().toString())
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
                                        .build()
                        )
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> serverError(Exception ex, ServerWebExchange exchange) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(
                                ErrorResponse.builder()
                                        .error("server_error")
                                        .errorDescription("Internal Server Error")
                                        .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                        .errorTimestamp(System.currentTimeMillis())
                                        .errorUri("https://developer.pamarin.com/document/error/")
                                        .errorCode(UUID.randomUUID().toString())
                                        .build()
                        )
        );
    }

}
