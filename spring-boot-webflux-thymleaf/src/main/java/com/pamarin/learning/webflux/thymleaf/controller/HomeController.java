/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.thymleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Controller
public class HomeController {

    @GetMapping({"", "/"})
    public Mono<String> hello(Model model) {
        model.addAttribute("name", "Jittagorn");
        return Mono.just("index");
    }
}
