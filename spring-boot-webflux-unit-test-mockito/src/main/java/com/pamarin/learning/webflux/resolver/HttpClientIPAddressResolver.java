/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.resolver;

import org.springframework.web.server.ServerWebExchange;


/**
 *
 * @author jitta
 */
public interface HttpClientIPAddressResolver {

    String resolve(ServerWebExchange exchange);

}
