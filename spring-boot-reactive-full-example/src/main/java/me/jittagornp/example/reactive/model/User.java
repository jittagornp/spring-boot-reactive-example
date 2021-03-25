package me.jittagornp.example.reactive.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class User {

    @ApiModelProperty(value = "id ผู้ใช้งาน")
    private UUID id;

    @ApiModelProperty(value = "บัญชีผู้ใช้งาน")
    private String username;

    @ApiModelProperty(value = "ชื่อใช้งาน")
    private String name;

}
