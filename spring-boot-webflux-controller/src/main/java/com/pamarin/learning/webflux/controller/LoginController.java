/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import com.pamarin.learning.webflux.model.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jitta
 */
@Slf4j
@RestController
public class LoginController {

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest req) {
        log.debug("username => {}", req.getUsername());
        log.debug("password => {}", req.getPassword());
    }

}
