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
public class CleanExpiredSessionJobRunner implements JobRunner {

    @Override
    @Scheduled(fixedDelay = 1000)
    public void run() {
        log.debug("clean expired session running...." + System.currentTimeMillis());
    }

}
