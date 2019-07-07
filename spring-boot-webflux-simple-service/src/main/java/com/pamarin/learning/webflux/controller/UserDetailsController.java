/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import com.pamarin.learning.webflux.dto.UserDetailsDto;
import com.pamarin.learning.webflux.exception.NotFoundException;
import com.pamarin.learning.webflux.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@RestController
public class UserDetailsController {

    private final UserDetailsService userDetailsService;

    @Autowired
    public UserDetailsController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping({"", "/"})
    public Flux<UserDetailsDto> home() {
        return findAll();
    }

    @GetMapping("/user-details")
    public Flux<UserDetailsDto> findAll() {
        return Flux.fromIterable(userDetailsService.findAll());
    }


    @GetMapping("/user-details/{id}")
    public Mono<UserDetailsDto> findById(@PathVariable("id") String id) {
        return Mono.justOrEmpty(userDetailsService.findByUserId(id))
                .switchIfEmpty(Mono.error(new NotFoundException("Not found user of id " + id)));
    }

}
