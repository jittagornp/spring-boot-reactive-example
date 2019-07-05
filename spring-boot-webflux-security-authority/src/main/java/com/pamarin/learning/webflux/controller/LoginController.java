/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Controller
public class LoginController {
    
    @GetMapping("/login")
    public Mono<String> login(){
        return Mono.just("custom-login");
    }
    
}