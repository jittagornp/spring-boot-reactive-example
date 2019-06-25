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
public class CleanSessionExpiredTaskRunner implements TaskRunner {

    @Override
    @Scheduled(fixedDelay = 1000)
    public void run() {
        log.debug("running...." + System.currentTimeMillis());
    }

}
