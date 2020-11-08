/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * @author jitta
 */
@Slf4j
@Configuration
public class LocaleConfig {

    @PostConstruct
    public void setDefaultLocale(){

        Locale.setDefault(Locale.forLanguageTag("th-TH"));

        log.info("set Default Locale => {}", Locale.getDefault());

    }

}
