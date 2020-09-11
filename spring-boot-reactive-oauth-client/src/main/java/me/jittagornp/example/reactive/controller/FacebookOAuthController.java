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
 * https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow
 *
 * @author jitta
 */
@Slf4j
@RestController
@RequestMapping("/facebook/oauth")
public class FacebookOAuthController extends OAuthControllerAdapter {

    @Override
    protected OAuthClient getOAuthClient() {
        return OAuthClient.builder()
                .clientId("314846xxxxxxxxxxxxxxxxxx")
                .clientSecret("647a877e6d5d9xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
                .scope("email")
                .build();
    }

    @Override
    protected String getAuthorizationCodeEndpoint() {
        return "https://www.facebook.com/v8.0/dialog/oauth";
    }

    @Override
    protected String getAccessTokenEndpoint() {
        return "https://graph.facebook.com/v8.0/oauth/access_token";
    }

    @Override
    protected String getUserInfoEndpoint() {
        return "https://graph.facebook.com/v8.0/me?fields=id,name,email";
    }

    @Override
    protected OAuthUserInfo mapUserInfo(final Map<String, Object> userInfo) {
        final String userId = (String) userInfo.get("id");
        return OAuthUserInfo.builder()
                .id(userId)
                .email((String) userInfo.get("email"))
                .name((String) userInfo.get("name"))
                .picture("https://graph.facebook.com/{userId}/picture?type=large".replace("{userId}", userId))
                .providerType(OAuthProviderType.FACEBOOK)
                .build();
    }

    @Override
    protected OAuthException parseErrorMessage(final Map<String, Object> error) {
        final Map map = (Map) error.get("error");
        return new OAuthException((String) map.get("message"));
    }

}
