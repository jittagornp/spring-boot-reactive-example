/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.entity;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

/**
 * @author jitta
 */
@Data
@Builder
public class UserEntity {

    private UUID id;

    private String username;

    private String firstName;

    private String lastName;
}
