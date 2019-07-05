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
import java.util.UUID;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import static org.springframework.util.StringUtils.hasText;

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

    private String schema(String table) {
        return schema + "." + table;
    }

    private String sql(String sql) {
        return String.format(sql, schema(User.TABLE_NAME));
    }

    @Override
    public List<User> findAll() {
        try {
            return jdbcTemplate.queryForObject(
                    sql("SELECT * FROM %s"),
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
                    sql("SELECT * FROM %s WHERE id = ?"),
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

    private boolean existsById(String id) {
        try {
            return jdbcTemplate.queryForObject(
                    sql("SELECT COUNT(id) FROM %s WHERE id = ?"),
                    new String[]{id},
                    String.class
            ) != null;
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }
    }

    private void insert(User user) {
        jdbcTemplate.update(
                sql("INSERT INTO %s (id, username, password) VALUES (?, ?, ?)"),
                new Object[]{
                    user.getId(),
                    user.getUsername(),
                    user.getPassword()
                }
        );
    }

    private void update(User user) {
        jdbcTemplate.update(
                sql("UPDATE %s SET username = ?, password = ? WHERE id = ?"),
                new Object[]{
                    user.getUsername(),
                    user.getPassword(),
                    user.getId()
                }
        );
    }

    private String randomId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public User save(User user) {
        Assert.notNull(user, "require user.");
        if (hasText(user.getId())) {
            if (existsById(user.getId())) {
                update(user);
            } else {
                insert(user);
            }
        } else {
            user.setId(randomId());
            insert(user);
        }

        return findById(user.getId()).get();
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(sql("DELETE FROM %s"));
    }

    @Override
    public void deleteById(String id) {
        jdbcTemplate.update(sql("DELETE FROM %s WHERE id = ?"), id);
    }
}
