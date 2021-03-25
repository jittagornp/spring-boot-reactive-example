/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import java.util.Map;

/**
 * @author jitta
 */
@Slf4j
@RequiredArgsConstructor
public class AuthTokenWebFilter implements WebFilter {

    private final AuthTokenService authTokenService;

    private final DefaultUserDetailsJwtClaimsConverter defaultUserDetailsJwtClaimsConverter;

    private final ServerSecurityContextRepository securityContextRepository;

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final String authorization = request.getHeaders().getFirst("Authorization");
        final String token = parseToken(authorization);
        if (hasText(token)) {
            return authTokenService.verify(token)
                    .flatMap(claims -> securityContextRepository.save(exchange, toSecurityContext(claims)))
                    .then(chain.filter(exchange));
        }
        return chain.filter(exchange);
    }

    private SecurityContext toSecurityContext(final Map<String, Object> claims) {
        final DefaultUserDetails userDetails = defaultUserDetailsJwtClaimsConverter.convert(claims);
        final SecurityContext securityContext = new SecurityContextImpl();
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authentication.setDetails(userDetails);
        securityContext.setAuthentication(authentication);
        return securityContext;
    }

    private String parseToken(final String authorization) {
        if (!hasText(authorization)) {
            return null;
        }
        final String[] arr = authorization.replaceAll("\\s+", " ").trim().split("\\s");
        if (!(arr != null && arr.length == 2)) {
            throw new AuthenticationException("Invalid token");
        }
        if (!"bearer".equalsIgnoreCase(arr[0])) {
            throw new AuthenticationException("Invalid bearer token");
        }
        return arr[1];
    }
}
