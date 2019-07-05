/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.repository;

import com.pamarin.learning.webflux.domain.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author jitta
 */
public interface UserRepository {

    List<User> findAll();

    Optional<User> findById(String id);

    User save(User user);

    void deleteAll();

    void deleteById(String id);
}
