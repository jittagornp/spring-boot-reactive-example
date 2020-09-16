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

    private String key = "NZMIT6TTDET2YBAX3GOT";

    private String secret = "GI7ZXBZBw7XUmTbnxRtTHHrHqYSrMyZBc9x6SESbHcs";

    private String endpoint = "sgp1.digitaloceanspaces.com";

    private String region = "sgp1";

    private String bucketName = "jittagornp-example";
}
