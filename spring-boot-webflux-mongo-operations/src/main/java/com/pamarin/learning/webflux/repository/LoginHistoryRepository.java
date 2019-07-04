/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.repository;

import com.pamarin.learning.webflux.collection.LoginHistory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
public interface LoginHistoryRepository {

    Mono<LoginHistory> save(LoginHistory history);

    Flux<LoginHistory> findAll();

    Mono<LoginHistory> findById(String id);

    Mono<Void> deleteById(String id);

    Mono<Void> deleteAll();

}
