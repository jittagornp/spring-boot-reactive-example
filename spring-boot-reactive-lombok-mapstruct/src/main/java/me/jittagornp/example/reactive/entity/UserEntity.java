/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jitta
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    private Long id;

    private String name;

    private Long createdAt;

}
