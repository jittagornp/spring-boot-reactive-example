/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jittagornp.example.reactive.model.ErrorResponse;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

/**
 * @author jitta
 */
@Slf4j
@Component
@Order(-2)
@RequiredArgsConstructor
public class ServerWebExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable e) {
        log.warn("error => ", e);
        final ErrorResponse err = ErrorResponse.serverError(e.getMessage());
        err.setErrorAt(LocalDateTime.now());
        err.setErrorTraceId(UUID.randomUUID().toString());
        err.setErrorUri("https://developer.pamarin.com/document/error/");
        return produceJson(err, exchange);
    }

    private void setHeaders(final ErrorResponse err, final ServerHttpResponse response){
        final HttpHeaders headers = response.getHeaders();
        response.setStatusCode(HttpStatus.valueOf(err.getErrorStatus()));
        try {
            headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
        } catch (UnsupportedOperationException e) {

        }
    }

    public Mono<Void> produceJson(final ErrorResponse err, final ServerWebExchange exchange) {
        return Mono.defer(() -> {
            try {
                final ServerHttpResponse response = exchange.getResponse();
                setHeaders(err, response);
                final String json = objectMapper.writeValueAsString(err);
                final DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(Charset.forName("utf-8")));
                return response.writeWith(Mono.just(buffer))
                        .doOnError(e -> DataBufferUtils.release(buffer));
            } catch (final Exception e) {
                return Mono.error(e);
            }
        });
    }

}

