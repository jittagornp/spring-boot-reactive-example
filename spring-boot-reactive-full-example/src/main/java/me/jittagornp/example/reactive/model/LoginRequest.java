/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jitta
 */
@Data
public class LoginRequest {

    @ApiModelProperty(value = "บัญชีผู้ใช้", position = 0)
    private String username;

    @ApiModelProperty(value = "รหัสผ่าน", position = 1)
    private String password;

}
