/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author jitta
 */
@Slf4j
@Component
public class CleanExpiredTokenJobRunner implements JobRunner {

    //cron format 
    //second, minute, hour, day of month, month, day(s) of week
    @Override
    @Scheduled(cron = "*/5 * * * * *") //every 5 seconds
    public void run() {
        log.debug("clean expired token running...." + System.currentTimeMillis());
    }

}
