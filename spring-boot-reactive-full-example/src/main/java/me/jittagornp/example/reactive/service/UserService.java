package me.jittagornp.example.reactive.service;

import me.jittagornp.example.reactive.model.CreateUser;
import me.jittagornp.example.reactive.model.PartialUpdateUser;
import me.jittagornp.example.reactive.model.UpdateUser;
import me.jittagornp.example.reactive.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface UserService {

    Mono<List<User>> findAllAsSlice(final Pageable pageable);

    Mono<Page<User>> findAllAsPage(final Pageable pageable);

    Mono<User> findById(final UUID id);

    Mono<User> create(final CreateUser user);

    Mono<User> update(final UpdateUser user);

    Mono<User> partialUpdate(final PartialUpdateUser user);

    Mono<Void> deleteById(final UUID id);

}
