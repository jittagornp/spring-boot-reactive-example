/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.model;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author jitta
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "require username")
    @Length(max = 50, message = "username more than {max} characters")
    private String username;

    @NotBlank(message = "require password")
    @Length(min = 8, max = 50, message = "password must between {min} to {max} characters")
    private String password;

}
