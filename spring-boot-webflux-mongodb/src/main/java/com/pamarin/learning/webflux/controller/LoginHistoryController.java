/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import com.pamarin.learning.webflux.collection.LoginHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import com.pamarin.learning.webflux.repository.LoginHistoryRepository;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@RestController
public class LoginHistoryController {

    private final LoginHistoryRepository repository;

    @Autowired
    public LoginHistoryController(LoginHistoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping({"", "/"})
    public Flux<LoginHistory> home() {
        return findAll();
    }

    @GetMapping("/histories")
    public Flux<LoginHistory> findAll() {
        return repository.findAll();
    }

    @GetMapping("/histories/{id}")
    public Mono<LoginHistory> findById(@PathVariable("id") String id) {
        return repository.findById(id);
    }

}
