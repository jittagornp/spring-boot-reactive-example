package me.jittagornp.example.reactive.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String authTokenSecretKey;

    private int authTokenExpiresMinutes;

    @PostConstruct
    public void showProperties() {

        log.debug("app.authTokenSecretKey => {}", authTokenSecretKey);
        log.debug("app.authTokenExpiresMinutes => {}", authTokenExpiresMinutes);

    }

}
