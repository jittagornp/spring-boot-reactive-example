/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.handler;

/**
 *
 * @author jitta
 */
public interface ErrorResponseExceptionHandlerResolver {

    ErrorResponseExceptionHandler resolve(Throwable e);

}
