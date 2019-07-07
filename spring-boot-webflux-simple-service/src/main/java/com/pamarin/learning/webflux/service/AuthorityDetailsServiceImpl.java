/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.service;

import com.pamarin.learning.webflux.domain.Authority;
import com.pamarin.learning.webflux.dto.AuthorityDetailsDto;
import com.pamarin.learning.webflux.repository.AuthorityRepository;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jitta
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AuthorityDetailsServiceImpl implements AuthorityDetailsService {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityDetailsServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public List<AuthorityDetailsDto> findAll() {
        return authorityRepository.findAll()
                .stream()
                .map(this::convertToAuthorityDetailsDto)
                .collect(toList());
    }

    @Override
    public Optional<AuthorityDetailsDto> findById(String id) {
        return authorityRepository.findById(id)
                .map(this::convertToAuthorityDetailsDto);
    }

    private AuthorityDetailsDto convertToAuthorityDetailsDto(Authority authority) {
        return AuthorityDetailsDto.builder()
                .id(authority.getId())
                .name(authority.getName())
                .description(authority.getDescription())
                .numberOfUsers(authority.getUserAuthorities().stream().count())
                .build();
    }
}
