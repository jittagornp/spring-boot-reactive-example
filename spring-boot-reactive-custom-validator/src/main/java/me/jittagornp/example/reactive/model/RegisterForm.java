/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.model;

import lombok.Data;
import me.jittagornp.example.reactive.validator.AtLeastPassword;
import me.jittagornp.example.reactive.validator.Email;
import me.jittagornp.example.reactive.validator.PasswordEqualsConfirmPassword;
import me.jittagornp.example.reactive.validator.PasswordNotEqualsEmail;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * @author jitta
 */
@Data
@PasswordEqualsConfirmPassword
@PasswordNotEqualsEmail
public class RegisterForm implements PasswordEqualsConfirmPassword.Model, PasswordNotEqualsEmail.Model {

    @NotBlank(message = "Required")
    @Email(message = "Invalid format")
    private String email;

    @NotBlank(message = "Required")
    @AtLeastPassword
    @Length(min = 8, max = 50, message = "At least {min} characters")
    private String password;

    @NotBlank(message = "Required")
    @Length(min = 8, max = 50, message = "At least {min} characters")
    private String confirmPassword;

}
