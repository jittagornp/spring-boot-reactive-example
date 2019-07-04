/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.repository;

import com.pamarin.learning.webflux.domain.User;
import java.util.List;

/**
 *
 * @author jitta
 */
public interface UserRepository {
 
    List<User> findAll();
    
    User findById(String id);
    
}
