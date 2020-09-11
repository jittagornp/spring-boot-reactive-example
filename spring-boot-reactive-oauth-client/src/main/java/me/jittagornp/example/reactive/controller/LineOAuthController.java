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
 * https://developers.line.biz/en/docs/line-login/integrate-line-login/
 *
 * @author jitta
 */
@Slf4j
@RestController
@RequestMapping("/line/oauth")
public class LineOAuthController extends OAuthControllerAdapter {

    @Override
    protected OAuthClient getOAuthClient() {
        return OAuthClient.builder()
                .clientId("16549xxxxxxx")
                .clientSecret("c5d28ca4axxxxxxxxxxxxxxxxxx")
                .scope("profile openid email")
                .build();
    }

    @Override
    protected String getAuthorizationCodeEndpoint() {
        return "https://access.line.me/oauth2/v2.1/authorize";
    }

    @Override
    protected String getAccessTokenEndpoint() {
        return "https://api.line.me/oauth2/v2.1/token";
    }

    @Override
    protected String getUserInfoEndpoint() {
        return null;
    }

    @Override
    protected OAuthUserInfo mapUserInfo(final Map<String, Object> userInfo) {
        final String userId = (String) userInfo.get("sub");
        return OAuthUserInfo.builder()
                .id(userId)
                .email((String) userInfo.get("email"))
                .name((String) userInfo.get("name"))
                .picture((String)userInfo.get("picture"))
                .providerType(OAuthProviderType.LINE)
                .build();
    }

    @Override
    protected OAuthException parseErrorMessage(final Map<String, Object> error) {
        return new OAuthException((String) error.get("error_description"));
    }
}
