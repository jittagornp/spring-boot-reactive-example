/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.config;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Slf4j
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/", "/login", "/logout").permitAll()
                .pathMatchers(HttpMethod.POST, "/users").hasAuthority("CREATE_USER")
                .pathMatchers(HttpMethod.PUT, "/users/{id}").hasAuthority("UPDATE_USER")
                .pathMatchers(HttpMethod.DELETE, "/users", "/users/{id}").hasAuthority("DELETE_USER")
                .pathMatchers(HttpMethod.GET, "/users/{id}/reset-password").hasAuthority("RESET_USER_PASSWORD")
                .anyExchange().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .logout()
                .logoutUrl("/logout")
                .requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/logout"))
                .and()
                .build();
    }

    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService(final PasswordEncoder passwordEncoder) {
        return username -> {
            log.debug("login with username => {}", username);

            UserDetails user;
            switch (username) {
                case "admin": {
                    user = User.withUsername(username)
                            .password(passwordEncoder.encode("password"))
                            .authorities(
                                    () -> "CREATE_USER",
                                    () -> "UPDATE_USER",
                                    () -> "DELETE_USER",
                                    () -> "RESET_USER_PASSWORD"
                            )
                            .build();
                    break;
                }

                case "supervisor": {
                    user = User.withUsername(username)
                            .password(passwordEncoder.encode("password"))
                            .authorities(
                                    () -> "RESET_USER_PASSWORD"
                            )
                            .build();
                    break;
                }

                default: {
                    user = User.withUsername(username)
                            .password(passwordEncoder.encode("password"))
                            .authorities(Collections.emptyList())
                            .build();
                }
            }

            return Mono.just(user);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
