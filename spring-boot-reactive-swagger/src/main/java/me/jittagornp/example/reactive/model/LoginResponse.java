/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author jitta
 */
@Data
@Builder
public class LoginResponse {

    @ApiModelProperty(value = "โทเค็น")
    @JsonProperty("access_token")
    private String accessToken;

    @ApiModelProperty(value = "ประเภทโทเค็น")
    private String type;

    @ApiModelProperty(value = "หมดอายุภายใน (วินาที)")
    @JsonProperty("expires_in")
    private Long expiresIn;

    //TODO
}
