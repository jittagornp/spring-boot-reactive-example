/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import lombok.RequiredArgsConstructor;
import me.jittagornp.example.reactive.entity.ProvinceEntity;
import me.jittagornp.example.reactive.repository.ProvinceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.relational.core.query.Query;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@RestController
@RequestMapping("/provinces")
@RequiredArgsConstructor
public class ProvinceController {

    private final ProvinceRepository provinceRepository;

    @GetMapping("/slice")
    public Flux<ProvinceEntity> findAllAsSlice() {
        final Pageable pageable = PageRequest.of(
                0,
                5,
                Sort.by(Sort.Order.by("name").with(Sort.Direction.ASC))
        );
        return provinceRepository.findAllAsSlice(Query.empty(), pageable);
    }

    @GetMapping("/page")
    public Mono<Page<ProvinceEntity>> findAllAsPage() {
        final Pageable pageable = PageRequest.of(
                0,
                5,
                Sort.by(Sort.Order.by("name").with(Sort.Direction.ASC))
        );
        return provinceRepository.findAllAsPage(Query.empty(), pageable);
    }

}
