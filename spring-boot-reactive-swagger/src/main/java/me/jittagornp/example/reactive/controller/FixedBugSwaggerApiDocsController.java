/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

/**
 * For FIXED bug duplicate model of Spring-fox Swagger
 *
 * @author jitta
 */
@Slf4j
@ApiIgnore
@Controller
@RequiredArgsConstructor
public class FixedBugSwaggerApiDocsController {

    private Map apiDocs;

    @ResponseBody
    @GetMapping("/swagger-resources")
    public Mono<List<Map<String, Object>>> getSwaggerResources() {
        final Map<String, Object> config = new HashMap<>();
        config.put("name", "default");
        config.put("url", "/v2/api-docs/_");
        config.put("swaggerVersion", "2.0");
        config.put("location", "/v2/api-docs/_");
        return Mono.just(Arrays.asList(config));
    }

    private String getBaseURL(final ServerWebExchange exchange){
        final String referer = exchange.getRequest().getHeaders().getFirst("Referer");
        try {
            final URL url = new URL(referer);
            return url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
        }catch (final Exception e){
            return null;
        }
    }

    @ResponseBody
    @GetMapping("/v2/api-docs/_")
    public Mono<Map> getApiDocs(final ServerWebExchange exchange) throws IOException {
        log.debug("GET api-docs");

        if (apiDocs != null) {
            return Mono.just(apiDocs);
        }

        final String baseURL = getBaseURL(exchange);
        return WebClient.create(baseURL + "/v2/api-docs")
                .get()
                .retrieve()
                .bodyToMono(Map.class)
                .map(data -> {
                    final Map<String, Object> map = (Map<String, Object>) data;
                    removeDuplicate$RefModels(map);
                    removeDuplicateModels(map);
                    apiDocs = map;
                    return map;
                });
    }

    private Map<String, Object> removeDuplicate$RefModels(final Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            if ("$ref".equals(key) && (value instanceof String)) {
                final String ref = ((String) value);
                if (ref.contains("_")) {
                    final String newRef = ref.split("_")[0];
                    log.debug("$ref => {} {}", ref, newRef);
                    entry.setValue(newRef);
                }
            }

            if (value instanceof Map) {
                removeDuplicate$RefModels((Map<String, Object>) value);
            } else if (value instanceof List) {
                final List list = (List) value;
                for (Object object : list) {
                    if (value instanceof Map) {
                        removeDuplicate$RefModels((Map<String, Object>) object);
                    }
                }
            }
        }

        return map;
    }

    private Map<String, Object> removeDuplicateModels(final Map<String, Object> map) {
        final Map<String, Object> definitions = (Map<String, Object>) map.get("definitions");
        final Iterator<Map.Entry<String, Object>> iterator = definitions.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, Object> entry = iterator.next();
            final String key = entry.getKey();
            if (key.contains("_")) {
                log.debug("duplicate model => {}", key);
                iterator.remove();
            }
        }
        return map;
    }

}
