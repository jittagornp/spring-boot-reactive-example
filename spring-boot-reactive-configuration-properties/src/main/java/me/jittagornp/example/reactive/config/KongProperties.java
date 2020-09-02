/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author jitta
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jittagornp.kong")
public class KongProperties {

    private String adminUrl;

    private ServiceRegistry serviceRegistry;

    public ServiceRegistry getServiceRegistry() {
        if (serviceRegistry == null) {
            serviceRegistry = new ServiceRegistry();
        }
        return serviceRegistry;
    }

    @Getter
    @Setter
    public static class ServiceRegistry {

        private String name;

        private String url;

        private List<String> routePaths;

        private boolean enabled;

        public List<String> getRoutePaths() {
            if (routePaths == null) {
                routePaths = new ArrayList<>();
                routePaths.add("/");
            }
            return routePaths;
        }

        public boolean getEnabled() {
            return enabled;
        }

    }
}
