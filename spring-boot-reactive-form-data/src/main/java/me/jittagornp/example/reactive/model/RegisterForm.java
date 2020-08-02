/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jitta
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {

    private String firstName;

    private String lastName;

    private String email;
}
