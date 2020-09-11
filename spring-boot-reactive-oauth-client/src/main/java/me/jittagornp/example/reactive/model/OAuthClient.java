/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author jitta
 */
@Data
@Builder
public class OAuthClient {

    private String clientId;

    private String clientSecret;

    private String scope;

}
