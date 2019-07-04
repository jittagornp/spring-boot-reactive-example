/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.repository;

import com.pamarin.learning.webflux.domain.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jitta
 */
@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final String schema;

    @Autowired
    public UserRepositoryImpl(
            DataSource dataSource,
            @Value("${spring.jpa.properties.hibernate.default_schema}") String schema
    ) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.schema = schema;
    }

    private String tableName(String table) {
        return schema + "." + table;
    }

    @Override
    public List<User> findAll() {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM " + tableName(User.TABLE_NAME),
                    (ResultSet rs, int i) -> {
                        List<User> list = new ArrayList<>();
                        do {
                            list.add(convertToEntity(rs));
                        } while (rs.next());
                        return list;
                    });
        } catch (EmptyResultDataAccessException ex) {
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<User> findById(String id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM " + tableName(User.TABLE_NAME) + " WHERE id = ?",
                    new Object[]{id},
                    (ResultSet rs, int i) -> convertToEntity(rs)
            ));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    private User convertToEntity(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getString("id"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .build();
    }
}
