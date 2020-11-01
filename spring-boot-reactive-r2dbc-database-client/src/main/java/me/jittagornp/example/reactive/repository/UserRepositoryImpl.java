/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.repository;

import lombok.RequiredArgsConstructor;
import me.jittagornp.example.reactive.entity.UserEntity;
import me.jittagornp.example.reactive.exception.NotFoundException;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;
import static org.springframework.data.relational.core.query.Criteria.where;

/**
 * @author jitta
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<UserEntity> findAll() {
        return databaseClient.select()
                .from(UserEntity.class)
                .fetch()
                .all();
    }

    @Override
    public Mono<UserEntity> findById(final UUID id) {
        return databaseClient.select()
                .from(UserEntity.class)
                .matching(where("id").is(id))
                .fetch()
                .one()
                .switchIfEmpty(Mono.error(new NotFoundException("User id \"" + id.toString() + "\"not found")));
    }

    @Override
    public Mono<UserEntity> create(final UserEntity entity) {
        entity.setId(UUID.randomUUID());
        return databaseClient.insert()
                .into(UserEntity.class)
                .using(entity)
                .then()
                .thenReturn(entity);
    }

    @Override
    public Mono<UserEntity> update(final UserEntity entity) {
        return findById(entity.getId())
                .flatMap(dbEntity -> {
                    return databaseClient.update()
                            .table(UserEntity.class)
                            .using(entity)
                            .then()
                            .thenReturn(entity);
                });
    }

    @Override
    public Mono<Void> deleteAll() {
        return databaseClient.delete()
                .from(UserEntity.class)
                .then();
    }

    @Override
    public Mono<Void> deleteById(final UUID id) {
        return findById(id)
                .flatMap(dbEntity -> {
                    return databaseClient.delete()
                            .from(UserEntity.class)
                            .matching(where("id").is(id))
                            .then();
                });
    }
}
