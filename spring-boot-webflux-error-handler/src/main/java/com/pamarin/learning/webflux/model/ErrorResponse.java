/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author jitta
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String error;

    private int errorStatus;

    private String errorDescription;

    private long errorTimestamp;

    private String errorUri;

    private String errorCode;

    private String state;

}
