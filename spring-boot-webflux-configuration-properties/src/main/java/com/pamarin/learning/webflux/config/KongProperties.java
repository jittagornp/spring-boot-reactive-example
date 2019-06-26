/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "pamarin.kong")
public class KongProperties {

    private String adminUrl;

    private ServiceRegistry serviceRegistry;

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

    public ServiceRegistry getServiceRegistry() {
        if (serviceRegistry == null) {
            serviceRegistry = new ServiceRegistry();
        }
        return serviceRegistry;
    }

}
