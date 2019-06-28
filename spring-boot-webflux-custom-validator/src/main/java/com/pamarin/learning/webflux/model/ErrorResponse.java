/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

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

    @JsonProperty("error_status")
    private int errorStatus;

    @JsonProperty("error_description")
    private String errorDescription;

    @JsonProperty("error_timestamp")
    private long errorTimestamp;

    @JsonProperty("error_uri")
    private String errorUri;

    @JsonProperty("error_code")
    private String errorCode;

    private String state;

    @JsonProperty("error_field")
    private List<Field> errorFields;

    public List<Field> getErrorFields() {
        if (errorFields == null) {
            errorFields = new ArrayList<>();
        }
        return errorFields;
    }

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Field {

        private String name;

        private String code;

        private String description;

    }

    /**
     * -------------------------------- 400 ------------------------------------
     */
    public static ErrorResponse invalidRequest(String message) {
        return builder()
                .error("invalid_request")
                .errorDescription(message)
                .errorStatus(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    public static ErrorResponse invalidRequest() {
        return invalidRequest("Invalid request");
    }

    /**
     * -------------------------------- 401 ------------------------------------
     */
    public static ErrorResponse unauthorized(String message) {
        return builder()
                .error("unauthorized")
                .errorDescription(message)
                .errorStatus(HttpStatus.UNAUTHORIZED.value())
                .build();
    }

    public static ErrorResponse unauthorized() {
        return unauthorized("Please login");
    }

    /**
     * -------------------------------- 403 ------------------------------------
     */
    public static ErrorResponse accessDenied(String message) {
        return builder()
                .error("access_denied")
                .errorDescription(message)
                .errorStatus(HttpStatus.FORBIDDEN.value())
                .build();
    }

    public static ErrorResponse accessDenied() {
        return accessDenied("Access denied");
    }

    /**
     * -------------------------------- 404 ------------------------------------
     */
    public static ErrorResponse notFound(String message) {
        return builder()
                .error("not_found")
                .errorDescription(message)
                .errorStatus(HttpStatus.NOT_FOUND.value())
                .build();
    }

    public static ErrorResponse notFound() {
        return notFound("Not found");
    }

    /**
     * -------------------------------- 500 ------------------------------------
     */
    public static ErrorResponse serverError() {
        return builder()
                .error("server_error")
                .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }
}
