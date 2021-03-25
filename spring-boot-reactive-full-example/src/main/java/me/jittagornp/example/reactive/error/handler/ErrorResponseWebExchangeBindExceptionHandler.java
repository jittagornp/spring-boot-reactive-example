/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.error.handler;

import me.jittagornp.example.reactive.error.ErrorResponse;
import me.jittagornp.example.reactive.error.ErrorResponseExceptionHandlerAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import com.google.common.base.CaseFormat;

/**
 * @author jitta
 */
@Component
public class ErrorResponseWebExchangeBindExceptionHandler extends ErrorResponseExceptionHandlerAdapter<WebExchangeBindException> {

    private final List<String> standardCodes = Arrays.asList("not_null", "not_blank");

    @Override
    public Class<WebExchangeBindException> getTypeClass() {
        return WebExchangeBindException.class;
    }

    @Override
    protected Mono<ErrorResponse> buildError(final ServerWebExchange exchange, final WebExchangeBindException ex) {
        return Mono.fromCallable(() -> {
            return ErrorResponse.builder()
                    .error("invalid_request")
                    .errorDescription("Validate fail")
                    .errorStatus(HttpStatus.BAD_REQUEST.value())
                    .errorAttributes(
                            ex.getFieldErrors()
                                    .stream()
                                    .map(f -> {
                                        return ErrorResponse.Attribute.builder()
                                                .name(f.getField())
                                                .code(replace(f.getCode()))
                                                .description(f.getDefaultMessage())
                                                .build();
                                    })
                                    .collect(toList())
                    )
                    .build();
        });
    }

    private String replace(final String code) {
        final String underscoreCode = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, code);
        for (String standardCode : standardCodes) {
            if (!underscoreCode.equals(standardCode) && underscoreCode.endsWith(standardCode)) {
                final int index = underscoreCode.indexOf(standardCode);
                return underscoreCode.substring(index);
            }
        }
        return underscoreCode;
    }

}