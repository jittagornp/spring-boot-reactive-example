package me.jittagornp.example.reactive.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class OAuthCodeRequest {

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "redirect_uri")
    private String redirectUri;

}
