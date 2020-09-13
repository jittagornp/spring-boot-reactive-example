/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import lombok.RequiredArgsConstructor;
import me.jittagornp.example.reactive.model.LoginRequest;
import me.jittagornp.example.reactive.validation.ManualValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jitta
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final ManualValidator manualValidator;

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest req)  {

        manualValidator.validate(req);

        log.debug("username => {}", req.getUsername());
        log.debug("password => {}", req.getPassword());
    }

}
