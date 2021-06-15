/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jittagornp.example.reactive.entity.UserEntity;
import me.jittagornp.example.reactive.mapper.UserMapper;
import me.jittagornp.example.reactive.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public Mono<User> findById(final Long id) {
        return Mono.fromCallable(() -> {
            //Do something
            final UserEntity entity = UserEntity.builder()
                    .id(id)
                    .name("Jittagorn Pitakmetagoon")
                    .createdAt(System.currentTimeMillis())
                    .build();
            log.debug("entity => {}", entity);
            return entity;
        }).map(userMapper::map);
    }

    @Override
    public Mono<User> create(final User user) {
        return Mono.fromCallable(() -> {
            final UserEntity entity = userMapper.map(user);
            entity.setId(1L);
            entity.setCreatedAt(System.currentTimeMillis());

            log.debug("entity => {}", entity);
            //Do something
            return entity;
        }).map(userMapper::map);
    }
}
