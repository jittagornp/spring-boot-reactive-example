/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pamarin.learning.webflux.model.ErrorResponse;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Slf4j
@Component
@Order(-2)
public class GlobalWebExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;
    
    private final ErrorResponseExceptionHandlerResolver resolver;

    @Autowired
    public GlobalWebExceptionHandler(ObjectMapper objectMapper, ErrorResponseExceptionHandlerResolver resolver) {
        this.resolver = resolver;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable e) {
        log.debug("Throwable class => {}", e.getClass().getName());
        ErrorResponse error = resolver.resolve(e).handle(exchange, e);
        error.setErrorUri("https://developer.pamarin.com/document/error/");
        return jsonResponse(
                ResponseEntity.status(HttpStatus.valueOf(error.getErrorStatus()))
                        .body(error),
                exchange
        );
    }

    public Mono<Void> jsonResponse(
            final ResponseEntity<ErrorResponse> entity,
            final ServerWebExchange exchange
    ) {
        final ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(entity.getStatusCode());
        response.getHeaders().addAll(entity.getHeaders());
        response.getHeaders().put(HttpHeaders.CONTENT_TYPE, Arrays.asList(MediaType.APPLICATION_JSON_UTF8_VALUE));
        try {
            final DataBuffer buffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(entity.getBody()));
            return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
        } catch (final JsonProcessingException ex) {
            return Mono.error(ex);
        }
    }
}
