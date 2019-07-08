/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author jitta
 */
@Data
@Entity
@Table(name = UserAuthority.TABLE_NAME)
public class UserAuthority implements Serializable {

    public static final String TABLE_NAME = "user_authority";

    @Data
    @Embeddable
    public static class UserAuthorityPK implements Serializable {

        @Column(name = "user_id")
        private String userId;

        @Column(name = "authority_id")
        private String authorityId;

    }

    @EmbeddedId
    private UserAuthorityPK id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "authority_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Authority authority;

}
