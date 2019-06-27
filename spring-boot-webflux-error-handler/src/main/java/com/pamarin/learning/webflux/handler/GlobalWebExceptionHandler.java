/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pamarin.learning.webflux.model.ErrorResponse;
import java.util.UUID;
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
@Component
@Order(-2)
public class GlobalWebExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Autowired
    public GlobalWebExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ResponseEntity<ErrorResponse> entity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(
                        ErrorResponse.builder()
                                .error("server_error")
                                .errorDescription("Internal Server Error")
                                .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .errorTimestamp(System.currentTimeMillis())
                                .errorUri("https://developer.pamarin.com/document/error/")
                                .errorCode(UUID.randomUUID().toString())
                                .build()
                );
        return setHttpResponse(entity, exchange);
    }

    public Mono<Void> setHttpResponse(
            final ResponseEntity<ErrorResponse> entity,
            final ServerWebExchange exchange
    ) {
        final ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(entity.getStatusCode());
        response.getHeaders().addAll(entity.getHeaders());
        try {
            final DataBuffer buffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(entity.getBody()));
            return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
        } catch (final JsonProcessingException ex) {
            return Mono.error(ex);
        }
    }
}
