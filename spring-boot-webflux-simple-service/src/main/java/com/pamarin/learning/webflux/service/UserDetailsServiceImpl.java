/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.service;

import com.pamarin.learning.webflux.domain.Authority;
import com.pamarin.learning.webflux.domain.User;
import com.pamarin.learning.webflux.domain.UserAuthority;
import com.pamarin.learning.webflux.dto.UserDetailsDto;
import com.pamarin.learning.webflux.repository.UserRepository;
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
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserDetailsDto> findByUserId(String id) {
        return userRepository.findById(id)
                .map(this::convertToUserDetailsDto);
    }

    private UserDetailsDto convertToUserDetailsDto(User user) {
        return UserDetailsDto.builder()
                .id(user.getId())
                .name(user.getUsername())
                .authorities(
                        user.getUserAuthorities()
                                .stream()
                                .map(this::convertToAuthorityDto)
                                .collect(toList())
                )
                .build();
    }

    private UserDetailsDto.AuthorityDto convertToAuthorityDto(UserAuthority userAuthority) {
        Authority authority = userAuthority.getAuthority();
        return UserDetailsDto.AuthorityDto.builder()
                .id(userAuthority.getId().getAuthorityId())
                .name(authority.getName())
                .description(authority.getDescription())
                .build();
    }

    @Override
    public List<UserDetailsDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToUserDetailsDto)
                .collect(toList());
    }

}
