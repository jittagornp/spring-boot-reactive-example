/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;
import lombok.RequiredArgsConstructor;

/**
 * @author jitta
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final TemplateEngine templateEngine;

    @GetMapping("/user")
    public Mono<ResponseEntity<String>> hello() {
        return Mono.fromCallable(() -> {
            final Context context = new Context();
            context.setVariable("name", "jittagorn pitakmetagoon");
            return ResponseEntity.ok()
                    .body(templateEngine.process("user", context));
        });
    }
}
