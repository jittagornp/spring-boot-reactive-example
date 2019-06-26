/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.model;

import com.pamarin.learning.webflux.validator.AtLeastPassword;
import com.pamarin.learning.webflux.validator.Email;
import com.pamarin.learning.webflux.validator.PasswordEqualsConfirmPassword;
import com.pamarin.learning.webflux.validator.PasswordNotEqualsEmail;
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
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordEqualsConfirmPassword
@PasswordNotEqualsEmail
public class RegisterForm implements PasswordEqualsConfirmPassword.Model, PasswordNotEqualsEmail.Model {

    @NotBlank(message = "require email")
    @Email(message = "invalid format")
    private String email;

    @AtLeastPassword
    @Length(min = 8, max = 50, message = "at least {min} characters")
    private String password;

    @Length(min = 8, max = 50, message = "at least {min} characters")
    private String confirmPassword;

}
