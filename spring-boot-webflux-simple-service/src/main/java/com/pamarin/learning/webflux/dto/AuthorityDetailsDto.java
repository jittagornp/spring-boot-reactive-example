/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.dto;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDetailsDto {

    private String id;

    private String name;

    private String description;
    
    private long numberOfUsers;

}
