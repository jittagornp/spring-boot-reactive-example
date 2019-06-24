/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.postgresql.repository;

import com.pamarin.learning.webflux.postgresql.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jitta
 */
public interface UserRepository extends JpaRepository<User, String>{
    
}
