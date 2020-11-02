/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.repository;

import lombok.RequiredArgsConstructor;
import me.jittagornp.example.reactive.entity.UserEntity;
import me.jittagornp.example.reactive.exception.NotFoundException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
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

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Flux<UserEntity> findAll() {
        final Query query = Query.empty();
        return r2dbcEntityTemplate.select(query, UserEntity.class);
    }

    @Override
    public Mono<UserEntity> findById(final UUID id) {
        final Query query = Query.query(where("id").is(id));
        return r2dbcEntityTemplate.select(query, UserEntity.class)
                .next()
                .switchIfEmpty(Mono.error(new NotFoundException("User id \"" + id.toString() + "\"not found")));
    }

    @Override
    public Mono<UserEntity> create(final UserEntity entity) {
        entity.setId(UUID.randomUUID());
        return r2dbcEntityTemplate.insert(entity);
    }

    @Override
    public Mono<UserEntity> update(final UserEntity entity) {
        return findById(entity.getId())
                .flatMap(dbEntity -> {
                    return r2dbcEntityTemplate.update(entity);
                });
    }

    @Override
    public Mono<Void> deleteAll() {
        final Query query = Query.empty();
        return r2dbcEntityTemplate.delete(query, UserEntity.class)
                .then();
    }

    @Override
    public Mono<Void> deleteById(final UUID id) {
        return findById(id)
                .flatMap(dbEntity -> {
                    final Query query = Query.query(where("id").is(id));
                    return r2dbcEntityTemplate.delete(query, UserEntity.class)
                            .then();
                });
    }
}
