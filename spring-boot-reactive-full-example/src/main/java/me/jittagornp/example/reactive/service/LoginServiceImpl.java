package me.jittagornp.example.reactive.service;

import lombok.RequiredArgsConstructor;
import me.jittagornp.example.reactive.config.AppProperties;
import me.jittagornp.example.reactive.exception.InvalidUsernamePasswordException;
import me.jittagornp.example.reactive.model.LoginRequest;
import me.jittagornp.example.reactive.model.LoginResponse;
import me.jittagornp.example.reactive.security.AuthTokenService;
import me.jittagornp.example.reactive.security.DefaultUserDetails;
import me.jittagornp.example.reactive.security.DefaultUserDetailsJwtClaimsConverter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final AppProperties appProperties;

    private final AuthTokenService authTokenService;

    private final DefaultUserDetailsJwtClaimsConverter defaultUserDetailsJwtClaimsConverter;

    @Override
    public Mono<LoginResponse> login(final LoginRequest request) {

        //TODO
        //Find and check user from database

        if(!("admin".equals(request.getUsername()) && "password".equals(request.getPassword()))){
            return Mono.error(new InvalidUsernamePasswordException());
        }

        final DefaultUserDetails userDetails = DefaultUserDetails.builder()
                .id(UUID.randomUUID())
                .authorities(Arrays.asList("ADMIN"))
                .build();

        final Map<String, Object> claims = defaultUserDetailsJwtClaimsConverter.convert(userDetails);

        return authTokenService.sign(claims)
                .map(token -> {
                    return LoginResponse.builder()
                            .expiresIn(appProperties.getAuthTokenExpiresMinutes() * 60L)
                            .tokenType("bearer")
                            .accessToken(token)
                            .build();
                });
    }

}
