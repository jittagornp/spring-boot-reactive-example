/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jittagornp.example.reactive.model.ErrorResponse;
import java.nio.charset.Charset;
import java.util.Collections;
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
import reactor.core.publisher.MonoSink;

/**
 * @author jitta
 */
@Slf4j
@Component
@Order(-2)
@RequiredArgsConstructor
public class ServerWebExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    private final ErrorResponseExceptionHandlerResolver resolver;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable e) {
        log.warn("error => ", e);
        return resolver.resolve(e)
                .flatMap(handler -> (Mono<ErrorResponse>)handler.handle(exchange, e))
                .flatMap(err -> {
                    return jsonResponse(
                            exchange,
                            err
                    );
                });
    }

    private Mono<Void> jsonResponse(final ServerWebExchange exchange, final ErrorResponse err) {
        final ServerHttpResponse response = exchange.getResponse();
        final HttpHeaders headers = response.getHeaders();
        response.setStatusCode(HttpStatus.valueOf(err.getErrorStatus()));
        try {
            headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
        } catch (UnsupportedOperationException e) {

        }
        return Mono.create((final MonoSink<String> callback) -> {
            try {
                final String json = objectMapper.writeValueAsString(err);
                callback.success(json);
            } catch (final Exception e) {
                callback.error(e);
            }
        })
                .flatMap(json -> {
                    final DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(Charset.forName("utf-8")));
                    return response.writeWith(Mono.just(buffer))
                            .doOnError(e -> DataBufferUtils.release(buffer));
                });
    }
}