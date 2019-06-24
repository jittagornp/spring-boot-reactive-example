/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 *
 * @author jitta
 */
@RestController
public class RedisOperationController {

    private final ReactiveRedisOperations<String, Object> redisOperations;

    @Autowired
    public RedisOperationController(ReactiveRedisOperations<String, Object> redisOperations) {
        this.redisOperations = redisOperations;
    }

    @GetMapping({"", "/"})
    public Flux<Object> findAll() {
        return redisOperations.keys("oauth2_refresh_token*")
                .flatMap(redisOperations.opsForValue()::get);
    }

}
