/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import com.auth0.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import me.jittagornp.example.reactive.exception.OAuthException;
import me.jittagornp.example.reactive.model.OAuthAccessToken;
import me.jittagornp.example.reactive.model.OAuthCodeRequest;
import me.jittagornp.example.reactive.model.OAuthClient;
import me.jittagornp.example.reactive.model.OAuthUserInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

/**
 * @author jitta
 */
@Slf4j
public abstract class OAuthControllerAdapter {

    protected abstract OAuthClient getOAuthClient();

    protected abstract String getAuthorizationCodeEndpoint();

    protected abstract String getAccessTokenEndpoint();

    protected abstract String getUserInfoEndpoint();

    protected abstract OAuthUserInfo mapUserInfo(final Map<String, Object> userInfo);

    protected abstract OAuthException parseErrorMessage(final Map<String, Object> error);

    @GetMapping("/authorize")
    public Mono<Void> authorize(
            @RequestParam("redirect_uri") final String redirectUri,
            final ServerWebExchange exchange
    ) {
        return Mono.fromRunnable(() -> {
            final ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
            response.getHeaders().setLocation(buildAuthorizeURI(redirectUri));
        });
    }

    @PostMapping("/token")
    public Mono<OAuthUserInfo> token(@RequestBody final OAuthCodeRequest request) {
        return getAccessToken(request)
                .flatMap(accessToken -> {
                    if (hasText(accessToken.getIdToken())) {
                        return getUserInfoByIdToken(accessToken.getIdToken());
                    } else {
                        return getUserInfoByAccessToken(accessToken.getAccessToken());
                    }
                });
    }

    private Mono<OAuthAccessToken> getAccessToken(final OAuthCodeRequest request) {
        final URI uri = URI.create(getAccessTokenEndpoint());
        final OAuthClient oauthClient = getOAuthClient();
        return WebClient.create()
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        BodyInserters.fromFormData("grant_type", "authorization_code")
                                .with("code", request.getCode())
                                .with("client_id", oauthClient.getClientId())
                                .with("client_secret", oauthClient.getClientSecret())
                                .with("redirect_uri", request.getRedirectUri())
                )
                .retrieve()
                .onStatus(HttpStatus::isError, (resp) -> convertError(uri, resp))
                .bodyToMono(OAuthAccessToken.class);
    }


    private Mono<OAuthUserInfo> getUserInfoByAccessToken(final String accessToken) {
        final URI uri = URI.create(getUserInfoEndpoint());
        return WebClient.create()
                .get()
                .uri(uri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::mapUserInfo);
    }

    private Mono<OAuthUserInfo> getUserInfoByIdToken(final String idToken) {
        return Mono.fromCallable(() -> {
            return mapUserInfo(
                    JWT.decode(idToken)
                            .getClaims()
                            .entrySet()
                            .stream()
                            .collect(Collectors.toMap(
                                    entry -> entry.getKey(),
                                    entry -> entry.getValue().as(Object.class)
                            ))
            );
        });
    }

    private Mono<? extends Throwable> convertError(final URI uri, final ClientResponse clientResponse) {
        return clientResponse.bodyToMono(Map.class)
                .flatMap(error -> {
                    try {
                        return Mono.error(parseErrorMessage(error));
                    } catch (Exception e) {
                        final String path = uri.getScheme() + "://" + uri.getHost() + uri.getPath();
                        return Mono.error(new OAuthException("Error on " + path));
                    }
                });
    }


    private URI buildAuthorizeURI(final String redirectUri) {
        return URI.create(getAuthorizationCodeEndpoint() + buildAuthorizationCodeQueryString(redirectUri));
    }

    private String buildAuthorizationCodeQueryString(final String redirectUri) {
        final OAuthClient oauthClient = getOAuthClient();
        return new StringBuilder()
                .append("?response_type=code")
                .append("&scope=").append(encodeURI(oauthClient.getScope()))
                .append("&client_id=").append(encodeURI(oauthClient.getClientId()))
                .append("&redirect_uri=").append(encodeURI(redirectUri))
                .append("&state=").append(UUID.randomUUID().toString()) //TODO
                .toString();
    }

    private String encodeURI(final String value) {
        try {
            return URLEncoder.encode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }
}
