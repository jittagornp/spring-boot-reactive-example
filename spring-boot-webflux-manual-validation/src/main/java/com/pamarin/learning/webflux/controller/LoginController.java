/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import com.pamarin.learning.webflux.model.LoginRequest;
import com.pamarin.learning.webflux.validation.ManualValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
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

    private final Validator validator;

    @Autowired
    public LoginController(Validator validator) {
        this.validator = validator;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest req) throws BindException, NoSuchMethodException {

        ManualValidation.of(req).validate(validator);

        log.debug("username => {}", req.getUsername());
        log.debug("password => {}", req.getPassword());
    }

}
