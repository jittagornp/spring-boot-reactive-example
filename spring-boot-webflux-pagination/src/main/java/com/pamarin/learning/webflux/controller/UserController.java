/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import com.pamarin.learning.webflux.domain.User;
import com.pamarin.learning.webflux.exception.NotFoundException;
import com.pamarin.learning.webflux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping({"", "/"})
    public Mono<Page<User>> home(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return findAll(page, size);
    }

    @GetMapping("/users")
    public Mono<Page<User>> findAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return Mono.just(userRepository.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/users/{id}")
    public Mono<User> findById(@PathVariable("id") String id) {
        return Mono.justOrEmpty(userRepository.findById(id))
                .switchIfEmpty(Mono.error(new NotFoundException("Not found user of id " + id)));
    }

    @PostMapping("/users")
    public Mono<User> save(@RequestBody User user) {
        return Mono.just(userRepository.save(user));
    }

    @DeleteMapping("/users")
    public Mono<Void> deleteAll() {
        userRepository.deleteAll();
        return Mono.empty();
    }

    @DeleteMapping("/users/{id}")
    public Mono<Void> deleteById(@PathVariable("id") String id) {
        userRepository.deleteById(id);
        return Mono.empty();
    }
}
