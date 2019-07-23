/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author jitta
 */
@Slf4j
public class FluxCollectListExample {
    
     public static void main(String[] args) {
        Mono<List<String>> list = Flux.just("1", "2", "3", "4", "5")
                .collectList();
        
                list.doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }
    
}
