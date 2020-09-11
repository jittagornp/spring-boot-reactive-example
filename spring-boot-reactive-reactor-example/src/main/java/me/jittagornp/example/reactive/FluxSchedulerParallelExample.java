package me.jittagornp.example.reactive;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * @author jitta
 */
@Slf4j
public class MonoSchedulerParallel {

    public static void main(String[] args) {

        Flux.interval(Duration.ofMinutes(1))
                .doOnNext(number -> {
                    try {
                        Thread.sleep(500);
                        log.debug("number => {}", number);
                    } catch (final InterruptedException e) {

                    }
                })
                .subscribeOn(Schedulers.parallel())
                .subscribe();

    }

}
