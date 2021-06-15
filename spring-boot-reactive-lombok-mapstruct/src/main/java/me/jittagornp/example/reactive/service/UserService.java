/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.service;

import me.jittagornp.example.reactive.model.User;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
public interface UserService {

    Mono<User> findById(final Long id);

    Mono<User> create(final User user);

}
