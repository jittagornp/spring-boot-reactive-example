/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.handler;

import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Slf4j
@Component
public class DefaultErrorResponseExceptionHandlerResolver implements ErrorResponseExceptionHandlerResolver {

    private final Map<Class, ErrorResponseExceptionHandler> registry;

    private final ErrorResponseRootErrorHandler rootErrorHandler;

    private final ErrorResponseRootExceptionHandler rootExceptionHandler;

    @Autowired
    public DefaultErrorResponseExceptionHandlerResolver(
            final List<ErrorResponseExceptionHandler> handlers,
            final ErrorResponseRootErrorHandler rootErrorHandler,
            final ErrorResponseRootExceptionHandler rootExceptionHandler
    ) {
        this.registry = handlers.stream()
                .filter(this::ignoreHandler)
                .collect(toMap(ErrorResponseExceptionHandler::getTypeClass, handler -> handler));
        this.rootErrorHandler = rootErrorHandler;
        this.rootExceptionHandler = rootExceptionHandler;
    }

    private boolean ignoreHandler(final ErrorResponseExceptionHandler handler) {
        return !(handler.getTypeClass() == Exception.class
                || handler.getTypeClass() == Error.class);
    }

    @Override
    public Mono<ErrorResponseExceptionHandler> resolve(final Throwable e) {
        ErrorResponseExceptionHandler handler = registry.get(e.getClass());
        if (handler == null) {
            if (e instanceof Error) {
                handler = rootErrorHandler;
            } else {
                handler = rootExceptionHandler;
            }
        }
        log.debug("handler => {}", handler.getClass().getName());
        return Mono.just(handler);
    }

}
