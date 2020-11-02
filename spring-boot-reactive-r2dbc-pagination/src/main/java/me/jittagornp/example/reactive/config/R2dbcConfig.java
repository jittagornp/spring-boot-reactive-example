/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

/**
 * @author jitta
 */
@Slf4j
@Configuration
public class R2dbcConfig {

    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(final DatabaseClient databaseClient){
        return new R2dbcEntityTemplate(databaseClient);
    }

}
