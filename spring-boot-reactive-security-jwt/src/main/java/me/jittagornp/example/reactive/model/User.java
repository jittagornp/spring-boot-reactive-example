/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.model;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

/**
 * @author jitta
 */
@Data
@Builder
public class User {

    private UUID id;

    private String name;

}