/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jittagornp.example.reactive.model.LoginRequest;
import me.jittagornp.example.reactive.model.LoginResponse;
import me.jittagornp.example.reactive.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = {"Authentication"}, description = "Authentication")
public class AuthController {

    private final LoginService loginService;

    @PostMapping("/login")
    @ApiOperation(value = "เข้าสู่ระบบ")
    public Mono<LoginResponse> login(@RequestBody final LoginRequest request) {
        return loginService.login(request);
    }
}
