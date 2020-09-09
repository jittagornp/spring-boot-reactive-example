/*
 * Copyright 2017-2019 Pamarin.com
 */
package me.jittagornp.example.reactive.handler;


import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
public interface ErrorResponseExceptionHandlerResolver {

    Mono<ErrorResponseExceptionHandler> resolve(final Throwable e);

}
