/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import com.pamarin.learning.webflux.dto.AuthorityDetailsDto;
import com.pamarin.learning.webflux.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.pamarin.learning.webflux.service.AuthorityDetailsService;

/**
 *
 * @author jitta
 */
@RestController
public class AuthorityDetailsController {

    private final AuthorityDetailsService authorityService;

    @Autowired
    public AuthorityDetailsController(AuthorityDetailsService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping("/authority-details")
    public Flux<AuthorityDetailsDto> findAll() {
        return Flux.fromIterable(authorityService.findAll());
    }

    @GetMapping("/authority-details/{id}")
    public Mono<AuthorityDetailsDto> findById(@PathVariable("id") String id) {
        return Mono.justOrEmpty(authorityService.findById(id))
                .switchIfEmpty(Mono.error(new NotFoundException("Not found authority of id " + id)));
    }
}
