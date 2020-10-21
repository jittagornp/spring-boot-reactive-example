/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.jittagornp.example.reactive.model.User;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Slf4j
@RestController
@RequestMapping("/users")
@Api(tags = {"User"}, description = "ผู้ใช้")
public class UserController {

    private final User ME = User.builder()
            .id(UUID.randomUUID().toString())
            .username("jittagornp")
            .password("test")
            .email("jittagornp@gmail.com")
            .build();

    @GetMapping
    @ApiOperation(value = "ดึงข้อมูลผู้ใช้ทั้งหมด")
    public Flux<User> findAll() {
        log.debug("find all users in database...");
        return Flux.just(
                ME,
                User.builder()
                        .id(UUID.randomUUID().toString())
                        .username("test")
                        .password("password")
                        .email("test@example.com")
                        .build()
        );
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "ดึงข้อมูลผู้ใช้ด้วย id")
    public Mono<User> findById(@PathVariable("id") final String id) {
        log.debug("find data by id in database...");
        return Mono.just(ME);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "สร้างข้อมูลผู้ใช้")
    public Mono<User> create(@RequestBody final User user){
        log.debug("insert data to database...");
        return Mono.just(user);
    }
    
    @PutMapping("/{id}")
    @ApiOperation(value = "อัพเดทข้อมูลผู้ใช้ด้วย id")
    public Mono<User> update(@PathVariable("id") final String id, @RequestBody final User user){
        log.debug("update data in database...");
        return Mono.just(user);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "ลบข้อมูลผู้ใช้ด้วย id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable("id") final String id) {
        log.debug("delete data in database...");
        return Mono.empty();
    }

    @GetMapping("/me")
    @ApiOperation(value = "ดึงข้อมูลผู้ใช้ของตัวเอง")
    public Mono<User> getUser() {
        return Mono.just(ME);
    }
}
