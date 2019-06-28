/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.handler;

import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Component
public class DefaultErrorResponseExceptionHandlerResolver implements ErrorResponseExceptionHandlerResolver {

    private final Map<Class, ErrorResponseExceptionHandler> registry;

    private final ErrorResponseRootExceptionHandler rootExceptionHandler;

    @Autowired
    public DefaultErrorResponseExceptionHandlerResolver(List<ErrorResponseExceptionHandler> handlers, ErrorResponseRootExceptionHandler rootExceptionHandler) {
        this.registry = handlers.stream()
                .filter(handler -> handler.getTypeClass() != Exception.class)
                .collect(toMap(ErrorResponseExceptionHandler::getTypeClass, handler -> handler));
        this.rootExceptionHandler = rootExceptionHandler;
    }

    @Override
    public ErrorResponseExceptionHandler resolve(Throwable e) {
        ErrorResponseExceptionHandler handler = registry.get(e.getClass());
        if (handler == null) {
            return rootExceptionHandler;
        }
        return handler;
    }

}
