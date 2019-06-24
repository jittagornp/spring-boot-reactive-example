/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.mongodb.controller;

import com.pamarin.learning.webflux.mongodb.collection.LoginHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import com.pamarin.learning.webflux.mongodb.repository.LoginHistoryRepository;

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

    @GetMapping({ "", "/" })
    public Flux<LoginHistory> findAll() {
        return repository.findAll();
    }

}
