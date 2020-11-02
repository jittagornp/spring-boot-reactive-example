/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.repository;

import lombok.RequiredArgsConstructor;
import me.jittagornp.example.reactive.entity.ProvinceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Collections;

/**
 * @author jitta
 */
@Repository
@RequiredArgsConstructor
public class ProvinceRepositoryImpl implements ProvinceRepository {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Flux<ProvinceEntity> findAllAsSlice(final Query query, final Pageable pageable) {
        final Query q = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .sort(pageable.getSort());
        return r2dbcEntityTemplate.select(q, ProvinceEntity.class);
    }

    @Override
    public Mono<Page<ProvinceEntity>> findAllAsPage(final Query query, final Pageable pageable) {
        final Mono<Long> count = r2dbcEntityTemplate.count(query, ProvinceEntity.class);
        final Flux<ProvinceEntity> slice = findAllAsSlice(query, pageable);
        return Mono.zip(count, slice.buffer().next().defaultIfEmpty(Collections.emptyList()))
                .map(output -> new PageImpl<>(
                        output.getT2(),
                        pageable,
                        output.getT1()
                ));
    }
}
