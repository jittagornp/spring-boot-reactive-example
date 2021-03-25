package me.jittagornp.example.reactive.service;

import lombok.extern.slf4j.Slf4j;
import me.jittagornp.example.reactive.model.CreateUser;
import me.jittagornp.example.reactive.model.PartialUpdateUser;
import me.jittagornp.example.reactive.model.UpdateUser;
import me.jittagornp.example.reactive.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Override
    public Mono<List<User>> findAllAsSlice(final Pageable pageable) {
        log.debug("findAllAsSlice");
        return Mono.fromCallable(() -> {
            return Arrays.asList(new User(), new User(), new User());
        });
    }

    @Override
    public Mono<Page<User>> findAllAsPage(final Pageable pageable) {
        log.debug("findAllAsPage");
        return Mono.fromCallable(() -> {
            final List<User> users = Arrays.asList(new User(), new User(), new User());
            return new PageImpl<>(
                    users,
                    pageable,
                    users.size()
            );
        });
    }

    @Override
    public Mono<User> findById(final UUID id) {
        log.debug("findById");
        return Mono.fromCallable(() -> {
            return new User();
        });
    }

    @Override
    public Mono<User> create(final CreateUser user) {
        log.debug("create");
        return Mono.fromCallable(() -> {
            return new User();
        });
    }

    @Override
    public Mono<User> update(final UpdateUser user) {
        log.debug("update");
        return Mono.fromCallable(() -> {
            return new User();
        });
    }

    @Override
    public Mono<User> partialUpdate(final PartialUpdateUser user) {
        log.debug("partialUpdate");
        return Mono.fromCallable(() -> {
            return new User();
        });
    }

    @Override
    public Mono<Void> deleteById(final UUID id) {
        log.debug("deleteById");
        return Mono.fromRunnable(() -> {

        });
    }
}
