/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import me.jittagornp.example.reactive.model.PaginationRequest;
import me.jittagornp.example.reactive.model.User;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    final long fakeTotalElements = 10;

    final List<User> fakeUsers = Arrays.asList(
            User.builder()
                    .id(UUID.randomUUID().toString())
                    .username("user1")
                    .password("*****")
                    .email("user1@example.com")
                    .build(),
            User.builder()
                    .id(UUID.randomUUID().toString())
                    .username("user2")
                    .password("*****")
                    .email("user2@example.com")
                    .build(),
            User.builder()
                    .id(UUID.randomUUID().toString())
                    .username("user3")
                    .password("*****")
                    .email("user3@example.com")
                    .build()
    );

    @GetMapping
    public Mono findAll(final PaginationRequest paginationRequest) {
        if (paginationRequest.isPageRequest()) {
            final Pageable pageable = paginationRequest.toPage();
            return fakePageFromDatabase(pageable);
        } else {
            final Pageable pageable = paginationRequest.toSlice();
            return fakeSliceFromDatabase(pageable);
        }
    }

    private Mono<Page<User>> fakePageFromDatabase(final Pageable pageable) {
        log.debug("Page *************************************");
        log.debug("offset => {}", pageable.getOffset());
        log.debug("limit => {}", pageable.getPageSize());
        log.debug("query from database ....");
        return Mono.just(new PageImpl<>(
                fakeUsers,
                pageable,
                fakeTotalElements
        ));
    }

    private Mono<List<User>> fakeSliceFromDatabase(final Pageable pageable) {
        log.debug("Slice *************************************");
        log.debug("offset => {}", pageable.getOffset());
        log.debug("limit => {}", pageable.getPageSize());
        log.debug("query from database ....");
        return Mono.just(fakeUsers);
    }

}
