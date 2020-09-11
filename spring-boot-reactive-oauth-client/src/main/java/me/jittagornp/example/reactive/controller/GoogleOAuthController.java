/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import lombok.extern.slf4j.Slf4j;
import me.jittagornp.example.reactive.enums.OAuthProviderType;
import me.jittagornp.example.reactive.exception.OAuthException;
import me.jittagornp.example.reactive.model.OAuthClient;
import me.jittagornp.example.reactive.model.OAuthUserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * https://developers.google.com/identity/protocols/oauth2/web-server
 *
 * @author jitta
 */
@Slf4j
@RestController
@RequestMapping("/google/oauth")
public class GoogleOAuthController extends OAuthControllerAdapter {

    @Override
    protected OAuthClient getOAuthClient() {
        return OAuthClient.builder()
                .clientId("3355xxxxxxxxxxx-xxxxxxxxxxxxxxxxxxxxxxxxxxxx.apps.googleusercontent.com")
                .clientSecret("D9pnYkQxxxxxxxxxxxxxxxxxx")
                .scope("profile email")
                .build();
    }

    @Override
    protected String getAuthorizationCodeEndpoint() {
        return "https://accounts.google.com/o/oauth2/v2/auth";
    }

    @Override
    protected String getAccessTokenEndpoint() {
        return "https://oauth2.googleapis.com/token";
    }

    @Override
    protected String getUserInfoEndpoint() {
        return "https://www.googleapis.com/oauth2/v2/userinfo?alt=json";
    }

    @Override
    protected OAuthUserInfo mapUserInfo(final Map<String, Object> userInfo) {
        return OAuthUserInfo.builder()
                .id((String) userInfo.get("id"))
                .email((String) userInfo.get("email"))
                .name((String) userInfo.get("name"))
                .picture((String) userInfo.get("picture"))
                .providerType(OAuthProviderType.GOOGLE)
                .build();
    }

    @Override
    protected OAuthException parseErrorMessage(final Map<String, Object> error) {
        return new OAuthException((String) error.get("error_description"));
    }

}
