/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<List<Object>> findAll() {
        Flux<String> keys = redisOperations.keys("oauth2_refresh_token*");
        return keys.flatMap(redisOperations.opsForValue()::get)
                .collectList();
    }

}
