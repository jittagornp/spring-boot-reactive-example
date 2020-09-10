/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import me.jittagornp.example.reactive.model.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jitta
 */
@Slf4j
@RestController
public class LoginController {

    @PostMapping("/login")
    public void login(@RequestBody @Validated LoginRequest req) {
        log.debug("username => {}", req.getUsername());
        log.debug("password => {}", req.getPassword());
    }

}
