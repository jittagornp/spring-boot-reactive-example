package me.jittagornp.example.reactive.controller;

import lombok.extern.slf4j.Slf4j;
import me.jittagornp.example.reactive.model.AccessToken;
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
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;

@Slf4j
public abstract class OAuthControllerAdapter {

    protected abstract OAuthClient getOAuthClient();

    protected abstract String getAuthorizationCodeEndpoint();

    protected abstract String getAccessTokenEndpoint();

    protected abstract String getUserInfoEndpoint();

    protected abstract OAuthUserInfo mapUserInfo(final Map<String, Object> userInfo);

    private URI buildRedirectURI(final String redirectUri) {
        return URI.create(getAuthorizationCodeEndpoint() + buildAuthorizationCodeQueryString(redirectUri));
    }

    @GetMapping("/authorize")
    public Mono<Void> authorize(
            @RequestParam("redirect_uri") final String redirectUri,
            final ServerWebExchange exchange
    ) {
        return Mono.fromRunnable(() -> {
            final ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
            response.getHeaders().setLocation(buildRedirectURI(redirectUri));
        });
    }

    @PostMapping("/token")
    public Mono<OAuthUserInfo> token(@RequestBody final OAuthCodeRequest request) {
        return getAccessToken(request)
                .map(AccessToken::getAccessToken)
                .flatMap(this::getUserInfo);
    }

    private Mono<AccessToken> getAccessToken(final OAuthCodeRequest request) {
        final URI uri = URI.create(getAccessTokenEndpoint() + buildAccessTokenQueryString(request));
        return WebClient.create()
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .bodyToMono(AccessToken.class);
    }

    private Mono<OAuthUserInfo> getUserInfo(final String accessToken) {
        final URI uri = URI.create(getUserInfoEndpoint());
        return WebClient.create()
                .get()
                .uri(uri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::mapUserInfo);
    }


    private String buildAuthorizationCodeQueryString(final String redirectUri) {
        final OAuthClient oauthClient = getOAuthClient();
        return new StringBuilder()
                .append("?response_type=code")
                .append("&scope=").append(encodeURI(oauthClient.getScope()))
                .append("&client_id=").append(encodeURI(oauthClient.getClientId()))
                .append("&redirect_uri=").append(encodeURI(redirectUri))
                .toString();
    }

    private String buildAccessTokenQueryString(final OAuthCodeRequest request) {
        final OAuthClient oauthClient = getOAuthClient();
        return new StringBuilder()
                .append("?grant_type=authorization_code")
                .append("&code=").append(encodeURI(request.getCode()))
                .append("&client_id=").append(encodeURI(oauthClient.getClientId()))
                .append("&client_secret=").append(encodeURI(oauthClient.getClientSecret()))
                .append("&redirect_uri=").append(encodeURI(request.getRedirectUri()))
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
