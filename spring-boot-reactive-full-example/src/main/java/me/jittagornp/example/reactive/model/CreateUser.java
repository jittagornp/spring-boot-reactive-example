package me.jittagornp.example.reactive.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class CreateUser {

    @ApiModelProperty(value = "บัญชีผู้ใช้งาน", required = true)
    @NotBlank(message = "Required")
    @Length(max = 50, message = "More than {max} characters")
    private String username;

    @ApiModelProperty(value = "ชื่อใช้งาน", required = true)
    @NotBlank(message = "Required")
    @Length(max = 50, message = "More than {max} characters")
    private String name;

}
