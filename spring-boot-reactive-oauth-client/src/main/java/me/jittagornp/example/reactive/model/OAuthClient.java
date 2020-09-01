package me.jittagornp.example.reactive.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuthClient {

    private String clientId;

    private String clientSecret;

    private String scope;

}
