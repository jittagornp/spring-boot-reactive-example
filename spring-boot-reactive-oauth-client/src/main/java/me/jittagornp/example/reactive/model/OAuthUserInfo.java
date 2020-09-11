/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.model;

import lombok.Builder;
import me.jittagornp.example.reactive.enums.OAuthProviderType;

import lombok.Data;

/**
 * @author jitta
 */
@Data
@Builder
public class OAuthUserInfo {

    private String id;

    private String name;

    private String email;

    private String picture;

    private OAuthProviderType providerType;


}
