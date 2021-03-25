package me.jittagornp.example.reactive.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Slf4j
@Configuration
public class TimeZoneConfig {

    @PostConstruct
    public void setDefaultTimeZone(){

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        log.info("set Default Time Zone => {}", TimeZone.getDefault().getID());

    }

}