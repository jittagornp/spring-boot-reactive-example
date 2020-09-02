/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Controller
public class IndexController {

    @GetMapping({"", "/"})
    public Mono<String> hello(final Model model) {
        model.addAttribute("name", "Jittagorn");
        return Mono.just("index");
    }
}
