/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import me.jittagornp.example.reactive.entity.UserEntity;
import me.jittagornp.example.reactive.exception.NotFoundException;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @author jitta
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<UserEntity> findAll() {
        return databaseClient.execute("SELECT * FROM app.user")
                .map(this::convert)
                .all();
    }

    @Override
    public Mono<UserEntity> findById(final UUID id) {
        return databaseClient.execute("SELECT * FROM app.user WHERE id = :id")
                .bind("id", id)
                .map(this::convert)
                .one()
                .switchIfEmpty(Mono.error(new NotFoundException("User id \"" + id.toString() + "\"not found")));
    }

    @Override
    public Mono<UserEntity> create(final UserEntity entity) {
        entity.setId(UUID.randomUUID());
        return databaseClient.execute(
                "INSERT INTO app.user (id, username, first_name, last_name) " +
                        "VALUES (:id, :username, :first_name, :last_name)"
        )
                .bind("id", entity.getId())
                .bind("username", entity.getUsername())
                .bind("first_name", entity.getFirstName())
                .bind("last_name", entity.getLastName())
                .then()
                .thenReturn(entity);
    }

    @Override
    public Mono<UserEntity> update(final UserEntity entity) {
        return findById(entity.getId())
                .flatMap(dbEntity -> {
                    return databaseClient.execute(
                            "UPDATE app.user " +
                                    "SET username = :username, first_name = :first_name, last_name = :last_name " +
                                    "WHERE id = :id"
                    )
                            .bind("id", entity.getId())
                            .bind("username", entity.getUsername())
                            .bind("first_name", entity.getFirstName())
                            .bind("last_name", entity.getLastName())
                            .then()
                            .thenReturn(entity);
                });
    }

    @Override
    public Mono<Void> deleteAll() {
        return databaseClient.execute("DELETE FROM app.user")
                .then();
    }

    @Override
    public Mono<Void> deleteById(final UUID id) {
        return findById(id)
                .flatMap(dbEntity -> {
                    return databaseClient.execute("DELETE FROM app.user WHERE id = :id")
                            .bind("id", id)
                            .then();
                });
    }

    private UserEntity convert(final Row row, final RowMetadata metadata) {
        return UserEntity.builder()
                .id(row.get("id", UUID.class))
                .username(row.get("username", String.class))
                .firstName(row.get("first_name", String.class))
                .lastName(row.get("last_name", String.class))
                .build();
    }
}
