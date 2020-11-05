/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * @author jitta
 */
@Slf4j
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {

    private final Algorithm algorithm;

    private final int expiresMinutes;

    @Override
    public Mono<String> sign(final Map<String, Object> claims) {
        return Mono.fromCallable(() -> {
            final Instant issuedAt = Instant.now();
            final Instant expiresAt = issuedAt.plus(Duration.ofMinutes(expiresMinutes));
            return JWT.create()
                    .withIssuedAt(Date.from(issuedAt))
                    .withExpiresAt(Date.from(expiresAt))
                    .withClaim("data", claims)
                    .sign(algorithm);
        });
    }

    @Override
    public Mono<Map<String, Object>> verify(final String token) {

        if (!StringUtils.hasText(token)) {
            return Mono.error(new AuthenticationException("Required token"));
        }

        return Mono.fromCallable(() -> {
            try {
                return JWT.require(algorithm)
                        .build()
                        .verify(token)
                        .getClaims()
                        .get("data")
                        .asMap();
            } catch (JWTVerificationException e) {
                log.warn("VerifyToken error => ", e);
                throw new AuthenticationException("Invalid token");
            }
        });
    }

}
