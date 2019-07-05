/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@RestController
public class HomeController {

    @GetMapping({"", "/"})
    public Mono<String> hello(Authentication authentication) {
        return Mono.just("Hello => " + (authentication == null ? "anonymous user" : authentication.getName()));
    }

    @PostMapping("/users")
    public Mono<String> createUser() {
        return Mono.just("Can create user.");
    }

    @PutMapping("/users/{id}")
    public Mono<String> udpateUser() {
        return Mono.just("Can update user.");
    }

    @DeleteMapping("/users/{id}")
    public Mono<String> deleteUser() {
        return Mono.just("Can delete user.");
    }

    @DeleteMapping("/users")
    public Mono<String> deleteAllUsers() {
        return Mono.just("Can delete all users.");
    }

    @PostMapping("/users/{id}/reset-password")
    public Mono<String> resetPassword() {
        return Mono.just("Can reset user password.");
    }
}
