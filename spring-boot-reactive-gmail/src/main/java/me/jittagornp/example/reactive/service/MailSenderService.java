/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.service;

import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
public interface MailSenderService {

    Mono<Void> sendEmail(final Message message);

    @Data
    @Builder
    public static class Message {

        private String subject;

        private String to;

        private String body;

    }

}
