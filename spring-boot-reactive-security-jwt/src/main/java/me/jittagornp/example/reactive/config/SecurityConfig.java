/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.config;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import me.jittagornp.example.reactive.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author jitta
 */
@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    //TODO
    //Move to application.properties
    private final String AUTH_TOKEN_SECRET_KEY = "<YOUR_KEY>";

    private final int AUTH_TOKEN_EXPIRES_MINUTES = 60 * 10; //10 Hr

    private final String[] PUBLIC_ACCESS_PATHS = new String[]{
            "/webjars/springfox-swagger-ui/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/v2/api-docs/_",
            "/swagger-resources",
            "/swagger-resources/**",
            "/favicon.ico",
            "/assets/**",
            "/public/**",
            "/auth/**",
            "/"
    };

    @Bean
    public AuthTokenService authTokenService() {
        final Algorithm algorithm = Algorithm.HMAC256(AUTH_TOKEN_SECRET_KEY);
        return new AuthTokenServiceImpl(algorithm, AUTH_TOKEN_EXPIRES_MINUTES);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        return http
                .securityContextRepository(new AuthServerSecurityContextRepository())
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .accessDeniedHandler(new AuthServerAccessDeniedHandler())
                )
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers(PUBLIC_ACCESS_PATHS).permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(
                        new AuthTokenWebFilter(
                                authTokenService(),
                                new DefaultUserDetailsJwtClaimsConverterImpl(),
                                new AuthServerSecurityContextRepository()
                        ),
                        SecurityWebFiltersOrder.AUTHENTICATION
                )
                .build();
    }


}
