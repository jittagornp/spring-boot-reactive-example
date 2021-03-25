package me.jittagornp.example.reactive.service;

import me.jittagornp.example.reactive.model.LoginRequest;
import me.jittagornp.example.reactive.model.LoginResponse;
import reactor.core.publisher.Mono;

public interface LoginService {

    Mono<LoginResponse> login(final LoginRequest request);

}
