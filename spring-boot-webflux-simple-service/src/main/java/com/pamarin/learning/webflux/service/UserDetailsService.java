/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.service;

import com.pamarin.learning.webflux.dto.UserDetailsDto;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author jitta
 */
public interface UserDetailsService {

    List<UserDetailsDto> findAll();

    Optional<UserDetailsDto> findByUserId(String id);

}
