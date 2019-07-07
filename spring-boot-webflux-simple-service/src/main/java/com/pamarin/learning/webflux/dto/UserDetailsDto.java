/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author jitta
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsDto {

    private String id;

    private String name;

    private List<AuthorityDto> authorities;

    public List<AuthorityDto> getAuthorities() {
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        return authorities;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorityDto {

        private String id;

        private String name;

        private String description;

    }

}
