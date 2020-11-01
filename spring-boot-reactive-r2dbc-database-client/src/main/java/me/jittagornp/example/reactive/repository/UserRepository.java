/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.repository;

import me.jittagornp.example.reactive.entity.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @author jitta
 */
public interface UserRepository {

    Flux<UserEntity> findAll();

    Mono<UserEntity> findById(final UUID id);

    Mono<UserEntity> create(final UserEntity entity);

    Mono<UserEntity> update(final UserEntity entity);

    Mono<Void> deleteAll();

    Mono<Void> deleteById(final UUID id);

}
