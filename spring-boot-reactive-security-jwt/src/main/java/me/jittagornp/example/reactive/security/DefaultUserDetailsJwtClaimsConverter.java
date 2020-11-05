/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.security;

import java.util.Map;

/**
 * @author jitta
 */
public interface DefaultUserDetailsJwtClaimsConverter {

    DefaultUserDetails convert(final Map<String, Object> claims);

    Map<String, Object> convert(final DefaultUserDetails userDetails);

}
