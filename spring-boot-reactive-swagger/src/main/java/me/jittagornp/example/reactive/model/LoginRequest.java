/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jitta
 */
@Data
@ApiModel(description = "คำขอเข้าสู่ระบบ")
public class LoginRequest {

    @ApiModelProperty(value = "บัญชีผู้ใช้", required = true, position = 0)
    private String username;

    @ApiModelProperty(value = "รหัสผ่าน", required = true, position = 1)
    private String password;

}
