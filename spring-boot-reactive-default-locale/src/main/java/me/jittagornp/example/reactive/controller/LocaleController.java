/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.Locale;

/**
 * @author jitta
 */
@RestController
@RequestMapping("/locales")
public class LocaleController {

    @GetMapping
    public Flux<Locale> getAvailableLocales(){
        return Flux.fromIterable(Arrays.asList(Locale.getAvailableLocales()));
    }

    @GetMapping("/default")
    public Mono<Locale> getDefault() {
        return Mono.just(Locale.getDefault());
    }

}
