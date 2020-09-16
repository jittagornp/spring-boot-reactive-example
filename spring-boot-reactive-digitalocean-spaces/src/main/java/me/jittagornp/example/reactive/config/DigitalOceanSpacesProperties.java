/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @author jitta
 */
@Getter
@Component
public class DigitalOceanSpacesProperties {

    private String key = "E74A6FBDxxxxxxxxxx";

    private String secret = "NjkqulQURJ3TAHLxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    private String endpoint = "sgp1.digitaloceanspaces.com";

    private String region = "sgp1";

    private String bucketName = "jittagornp-example";
}
