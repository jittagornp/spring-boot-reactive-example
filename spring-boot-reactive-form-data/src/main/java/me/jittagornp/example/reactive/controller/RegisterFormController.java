/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import me.jittagornp.example.reactive.model.RegisterForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Slf4j
@RestController
public class RegisterFormController {

    @GetMapping({"", "/"})
    public Mono<String> hello() {
        return Mono.just("Hello world.");
    }

    @PostMapping(value = "/register1", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public Mono<RegisterForm> register1(final RegisterForm form) {
        return Mono.just(form);
    }

    @PostMapping(value = "/register2", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public Mono<RegisterForm> register2(final ServerWebExchange exchange) {
        return exchange.getFormData()
                .map(formData -> {
                    return RegisterForm.builder()
                            .firstName(formData.getFirst("firstName"))
                            .lastName(formData.getFirst("lastName"))
                            .email(formData.getFirst("email"))
                            .build();
                });
    }

    @PostMapping(value = "/register3", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Mono<RegisterForm> register3(final RegisterForm form) {
        return Mono.just(form);
    }
}
