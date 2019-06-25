/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PrintLogWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.debug("filter working..");
        return chain.filter(exchange);
    }
    
}
