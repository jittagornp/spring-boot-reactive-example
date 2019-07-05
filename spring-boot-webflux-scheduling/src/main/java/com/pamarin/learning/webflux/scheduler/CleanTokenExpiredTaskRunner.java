/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Slf4j
@Component
public class CleanTokenExpiredTaskRunner implements TaskRunner {

    //cron format 
    //second, minute, hour, day of month, month, day(s) of week
    @Override
    @Scheduled(cron = "*/5 * * * * *") //every 5 seconds
    public void run() {
        log.debug("clean token() running...." + System.currentTimeMillis());
    }

}
