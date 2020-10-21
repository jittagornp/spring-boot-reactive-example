/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author jitta
 */
@Data
@Builder
@ApiModel(description = "ผู้ใช้งาน")
public class User {

    @ApiModelProperty(value = "id ผู้ใช้งาน", position = 0)
    private String id;

    @ApiModelProperty(value = "บัญชีผู้ใช้งาน", position = 1)
    private String username;

    @ApiModelProperty(value = "รหัสผ่าน", position = 2)
    private String password;

    @ApiModelProperty(value = "อีเมล", position = 3)
    private String email;

}
