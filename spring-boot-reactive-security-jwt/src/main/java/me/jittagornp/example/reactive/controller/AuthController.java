/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jittagornp.example.reactive.model.LoginRequest;
import me.jittagornp.example.reactive.model.LoginResponse;
import me.jittagornp.example.reactive.security.AuthTokenService;
import me.jittagornp.example.reactive.security.DefaultUserDetails;
import me.jittagornp.example.reactive.security.DefaultUserDetailsJwtClaimsConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * @author jitta
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    //TODO
    //Move to application.properties
    private final int AUTH_TOKEN_EXPIRES_MINUTES = 60 * 10; //10 Hr

    private final AuthTokenService authTokenService;

    private final DefaultUserDetailsJwtClaimsConverter defaultUserDetailsJwtClaimsConverter;

    @PostMapping("/login")
    public Mono<LoginResponse> login(@RequestBody final LoginRequest request) {
        //TODO
        final DefaultUserDetails userDetails = DefaultUserDetails.builder()
                .id(UUID.randomUUID())
                .authorities(Arrays.asList("ADMIN"))
                .build();
        final Map<String, Object> claims = defaultUserDetailsJwtClaimsConverter.convert(userDetails);

        return authTokenService.sign(claims)
                .map(token -> {
                    return LoginResponse.builder()
                            .expiresIn(AUTH_TOKEN_EXPIRES_MINUTES * 60L)
                            .tokenType("bearer")
                            .accessToken(token)
                            .build();
                });
    }
}
