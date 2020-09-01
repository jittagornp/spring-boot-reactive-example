package me.jittagornp.example.reactive.model;

import lombok.Builder;
import me.jittagornp.example.reactive.enums.SocialMediaType;

import lombok.Data;

@Data
@Builder
public class OAuthUserInfo {

    private String id;

    private String name;

    private String email;

    private String picture;

    private SocialMediaType socialMediaType;


}
