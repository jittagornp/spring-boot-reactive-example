/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author jitta
 */
@Data
@Builder
public class User {

    private String id;

    private String username;

    private String password;

    private String email;

}
