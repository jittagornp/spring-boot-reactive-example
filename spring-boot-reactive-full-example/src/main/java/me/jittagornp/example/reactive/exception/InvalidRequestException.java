/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.exception;

/**
 * @author jitta
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException() {
    }

    public InvalidRequestException(String message) {
        super(message);
    }
}