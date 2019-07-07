/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.exception;

/**
 *
 * @author jitta
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

}
