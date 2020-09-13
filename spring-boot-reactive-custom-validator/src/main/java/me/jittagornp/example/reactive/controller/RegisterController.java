/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import me.jittagornp.example.reactive.model.RegisterForm;
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
public class RegisterController {

    @PostMapping("/register")
    public void register(@RequestBody @Validated RegisterForm req) {
        log.debug("email => {}", req.getEmail());
        log.debug("password => {}", req.getPassword());
    }

}
