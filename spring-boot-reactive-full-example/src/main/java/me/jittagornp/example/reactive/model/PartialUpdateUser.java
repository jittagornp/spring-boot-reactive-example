package me.jittagornp.example.reactive.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class PartialUpdateUser {

    @ApiModelProperty(value = "id ผู้ใช้งาน")
    private UUID id;

    @ApiModelProperty(value = "บัญชีผู้ใช้งาน")
    @Length(max = 50, message = "More than {max} characters")
    private String username;

    @ApiModelProperty(value = "ชื่อใช้งาน")
    @Length(max = 50, message = "More than {max} characters")
    private String name;

}
