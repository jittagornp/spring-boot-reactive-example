/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.jittagornp.example.reactive.model.LoginRequest;
import me.jittagornp.example.reactive.model.LoginResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 *
 * @author jitta
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Api(tags = {"Authentication"}, description = "ยืนยันตัวตน")
public class AuthController {

    @PostMapping("/login")
    @ApiOperation(value = "เข้าสู่ระบบ")
    public Mono<LoginResponse> login(final @RequestBody @Validated LoginRequest request) {
        return Mono.just(
                LoginResponse.builder()
                        .accessToken(UUID.randomUUID().toString())
                        .type("bearer")
                        .expiresIn(60 * 60L) // 1Hr
                        .build()
        );
    }

}