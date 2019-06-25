/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Controller
public class UserController {

    private final TemplateEngine templateEngine;

    @Autowired
    public UserController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @GetMapping("/user")
    public Mono<ResponseEntity<String>> hello() {
        Context context = new Context();
        context.setVariable("name", "jittagorn pitakmetagoon");
        return Mono.just(ResponseEntity.ok().body(templateEngine.process("user", context)));
    }
}
