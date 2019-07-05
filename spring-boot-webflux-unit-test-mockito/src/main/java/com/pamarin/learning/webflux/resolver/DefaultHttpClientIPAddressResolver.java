/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.resolver;

import java.net.InetSocketAddress;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.server.ServerWebExchange;

/**
 *
 * @author jitta
 */
@Slf4j
@Component
public class DefaultHttpClientIPAddressResolver implements HttpClientIPAddressResolver {

    private static final String CACHED_KEY = HttpClientIPAddressResolver.class.getName() + ".IP_ADDRESS";

    private static final String[] IP_ADDRESS_HEADERS = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR"
    };

    private static boolean has(String ip) {
        if (!hasText(ip)) {
            return false;
        }

        return !"UNKNOWN".equalsIgnoreCase(ip);
    }

    @Override
    public String resolve(ServerWebExchange exchange) {
        Assert.notNull(exchange, "require exchange.");
        String cached = (String) exchange.getAttribute(CACHED_KEY);
        if (has(cached)) {
            return cached;
        }

        ServerHttpRequest httpReq = exchange.getRequest();
        HttpHeaders headers = httpReq.getHeaders();
        for (String header : IP_ADDRESS_HEADERS) {
            String ip = headers.getFirst(header);
            if (has(ip)) {
                return cached(ip, exchange);
            }
        }

        return cached(getRemoteAddress(httpReq), exchange);
    }

    private String getRemoteAddress(ServerHttpRequest httpReq) {
        InetSocketAddress removeAddress = httpReq.getRemoteAddress();
        if (removeAddress == null) {
            return null;
        }
        return removeAddress.toString();
    }

    private String cached(String ip, ServerWebExchange exchange) {
        Map<String, Object> attributes = exchange.getAttributes();
        if (hasText(ip)) {
            attributes.put(CACHED_KEY, ip);
        } else {
            attributes.remove(CACHED_KEY, ip);
        }
        return ip;
    }
}
