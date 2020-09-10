/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.handler;


import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
public interface ErrorResponseExceptionHandlerResolver {

    Mono<ErrorResponseExceptionHandler> resolve(final Throwable e);

}
