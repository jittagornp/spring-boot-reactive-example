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
}
