/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.mongodb.repository;

import com.pamarin.learning.webflux.mongodb.collection.LoginHistory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


/**
 *
 * @author jitta
 */
public interface LoginHistoryRepository extends ReactiveMongoRepository<LoginHistory, String>{
    
}
