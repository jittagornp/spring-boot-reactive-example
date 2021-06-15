/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import lombok.RequiredArgsConstructor;
import me.jittagornp.example.reactive.model.User;
import me.jittagornp.example.reactive.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public Mono<User> create(@RequestBody final User user) {
        return userService.create(user);
    }

    @GetMapping("/{id}")
    public Mono<User> findById(@PathVariable("id") final Long id) {
        return userService.findById(id);
    }
}
