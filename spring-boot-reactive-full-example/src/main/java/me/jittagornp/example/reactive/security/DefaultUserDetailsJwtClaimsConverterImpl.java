/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author jitta
 */
@Component
public class DefaultUserDetailsJwtClaimsConverterImpl implements DefaultUserDetailsJwtClaimsConverter {

    @Override
    public DefaultUserDetails convert(final Map<String, Object> claims) {
        return DefaultUserDetails.builder()
                .id(UUID.fromString((String) claims.get("id")))
                .authorities((List<String>) claims.get("authorities"))
                .build();
    }

    @Override
    public Map<String, Object> convert(final DefaultUserDetails userDetails) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getUsername());
        claims.put("authorities",
                userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );
        return claims;
    }
}
