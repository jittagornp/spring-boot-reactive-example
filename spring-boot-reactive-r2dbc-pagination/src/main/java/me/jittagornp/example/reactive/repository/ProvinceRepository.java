/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.repository;

import me.jittagornp.example.reactive.entity.ProvinceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
public interface ProvinceRepository {

    Flux<ProvinceEntity> findAllAsSlice(final Query query, final Pageable pageable);

    Mono<Page<ProvinceEntity>> findAllAsPage(final Query query, final Pageable pageable);

}
