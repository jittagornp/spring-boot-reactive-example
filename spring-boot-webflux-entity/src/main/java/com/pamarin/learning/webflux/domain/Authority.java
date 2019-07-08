/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author jitta
 */
@Data
@Entity
@Table(name = Authority.TABLE_NAME)
public class Authority implements Serializable {

    public static final String TABLE_NAME = "authority";

    @Id
    private String id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authority")
    private List<UserAuthority> userAuthorities;

    public List<UserAuthority> getUserAuthorities() {
        if (userAuthorities == null) {
            userAuthorities = new ArrayList<>();
        }
        return userAuthorities;
    }

}
