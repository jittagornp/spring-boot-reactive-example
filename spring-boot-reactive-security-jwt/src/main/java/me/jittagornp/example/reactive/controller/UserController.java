/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import me.jittagornp.example.reactive.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.util.UUID;

/**
 * @author jitta
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public Flux<User> findAll() {
        return Flux.just(
                User.builder()
                        .id(UUID.randomUUID())
                        .name("user 1")
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .name("user 2")
                        .build(),
                User.builder()
                        .id(UUID.randomUUID())
                        .name("user 3")
                        .build()
        );
    }
}
