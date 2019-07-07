/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.service;

import com.pamarin.learning.webflux.dto.AuthorityDetailsDto;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author jitta
 */
public interface AuthorityDetailsService {

    List<AuthorityDetailsDto> findAll();

    Optional<AuthorityDetailsDto> findById(String id);

}
