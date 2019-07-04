/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.repository;

import com.pamarin.learning.webflux.collection.LoginHistory;
import com.pamarin.learning.webflux.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Repository
public class LoginHistoryRepositoryImpl implements LoginHistoryRepository {

    private final MongoOperations mongoOperations;

    @Autowired
    public LoginHistoryRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Flux<LoginHistory> findAll() {
        return Flux.fromIterable(mongoOperations.findAll(LoginHistory.class));
    }

    @Override
    public Mono<LoginHistory> findById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return Mono.justOrEmpty(mongoOperations.findOne(query, LoginHistory.class))
                .switchIfEmpty(Mono.error(new NotFoundException("Not found user of id " + id)));
    }

    @Override
    public Mono<LoginHistory> save(LoginHistory history) {
        return Mono.just(mongoOperations.save(history));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return Mono.just(mongoOperations.remove(query, LoginHistory.class))
                .then();
    }

    @Override
    public Mono<Void> deleteAll() {
        mongoOperations.dropCollection(LoginHistory.class);
        return Mono.empty();
    }

}
