/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.security;

import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
public class AuthServerSecurityContextRepository implements ServerSecurityContextRepository {

    private static final String SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    private static final Authentication ANONYMOUS = new AnonymousAuthentication();

    @Override
    public Mono<Void> save(final ServerWebExchange exchange, final SecurityContext securityContext) {
        return Mono.fromRunnable(() -> {
            final Map<String, Object> attributes = exchange.getAttributes();
            if (securityContext == null) {
                attributes.remove(SECURITY_CONTEXT_KEY);
            } else {
                attributes.put(SECURITY_CONTEXT_KEY, securityContext);
            }
        });
    }

    @Override
    public Mono<SecurityContext> load(final ServerWebExchange exchange) {
        return Mono.fromCallable(() -> {
            final SecurityContext securityContext = (SecurityContext) exchange.getAttributes().get(SECURITY_CONTEXT_KEY);
            if (securityContext == null) {
                return new SecurityContextImpl(ANONYMOUS);
            }
            return securityContext;
        });
    }

}
