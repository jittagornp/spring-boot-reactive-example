/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.handler;

import lombok.extern.slf4j.Slf4j;
import me.jittagornp.example.reactive.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Slf4j
@Component
public class ErrorResponseResponseStatusExceptionHandler extends ErrorResponseExceptionHandlerAdapter<ResponseStatusException> {

    @Override
    public Class<ResponseStatusException> getTypeClass() {
        return ResponseStatusException.class;
    }

    @Override
    protected Mono<ErrorResponse> buildError(final ServerWebExchange exchange, final ResponseStatusException e) {
        return Mono.fromCallable(() -> {
            //400
            if (e.getStatus() == HttpStatus.BAD_REQUEST) {

                final String message = e.getMessage();
                if(message.contains("Request body is missing")){
                    return ErrorResponse.invalidRequest("invalid body format");
                }

                return ErrorResponse.invalidRequest(message);
            }
            //401
            if (e.getStatus() == HttpStatus.UNAUTHORIZED) {
                return ErrorResponse.unauthorized(e.getMessage());
            }
            //403
            if (e.getStatus() == HttpStatus.FORBIDDEN) {
                return ErrorResponse.accessDenied(e.getMessage());
            }
            //404
            if (e.getStatus() == HttpStatus.NOT_FOUND) {
                log.debug("Not found => {}", exchange.getRequest().getPath());
                return ErrorResponse.notFound("not found");
            }
            return ErrorResponse.serverError();
        });
    }
}

