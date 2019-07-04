/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import com.pamarin.learning.webflux.domain.User;
import com.pamarin.learning.webflux.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@RestController
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public Flux<User> findAll() {
        return Flux.fromIterable(userRepository.findAll());
    }

    @GetMapping("/users/{id}")
    public Mono<User> findById(@PathVariable("id") String id) {
        return Mono.just(userRepository.findById(id)
                .orElse(null));
    }

}
