/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.postgresql.controller;

import com.pamarin.learning.webflux.postgresql.domain.User;
import com.pamarin.learning.webflux.postgresql.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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

}
