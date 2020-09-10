/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * @author jitta
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @JsonProperty("error")
    private String error;

    @JsonProperty("error_status")
    private int errorStatus;

    @JsonProperty("error_description")
    private String errorDescription;

    @JsonProperty("error_at")
    private LocalDateTime errorAt;

    @JsonProperty("error_trace_id")
    private String errorTraceId;

    @JsonProperty("error_uri")
    private String errorUri;

    @JsonProperty("error_on")
    private String errorOn;

    @JsonProperty("error_fields")
    private List<Field> errorFields;

    @JsonProperty("error_data")
    private Map<String, Object> errorData;

    @JsonProperty("state")
    private String state;

    public String getErrorDescription() {
        return protectSensitiveErrorDescription(errorDescription);
    }

    public List<Field> getErrorFields() {
        if (errorFields == null) {
            errorFields = new ArrayList<>();
        }
        return errorFields;
    }

    public Map<String, Object> getErrorData() {
        if (errorData == null) {
            errorData = new HashMap<>();
        }
        return errorData;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Field {

        private String name;

        private String code;

        private String description;

        public String getDescription() {
            return protectSensitiveErrorDescription(description);
        }

    }

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

    public static ErrorResponse serverError(String message) {
        return builder()
                .error("server_error")
                .errorDescription(message)
                .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }

    public static ErrorResponse serverError() {
        return serverError("Unknown error");
    }

    private static String protectSensitiveErrorDescription(final String description) {
        if (description != null) {
            final String desc = description.toLowerCase();
            if (desc.contains("java")
                    || desc.contains("springframework")
                    || desc.contains("sql")
                    || desc.contains("mongo")) {
                return "Unknown error";
            }
        }
        return description;
    }
}