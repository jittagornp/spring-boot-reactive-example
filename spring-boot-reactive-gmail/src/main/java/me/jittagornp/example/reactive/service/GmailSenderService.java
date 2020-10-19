/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jittagornp.example.reactive.exception.MailSenderException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.mail.javamail.JavaMailSender;
import reactor.core.scheduler.Schedulers;

import javax.mail.internet.MimeMessage;

/**
 * @author jitta
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GmailSenderService implements MailSenderService {

    private final JavaMailSender javaMailSender;

    @Override
    public Mono<Void> sendEmail(final Message message) {
        return Mono.fromRunnable(() -> {
            try {
                final MimeMessage msg = javaMailSender.createMimeMessage();

                // true = multipart message
                final MimeMessageHelper helper = new MimeMessageHelper(msg, true);
                helper.setTo(message.getTo());

                helper.setSubject(message.getSubject());

                // true = text/html
                helper.setText(message.getBody(), true);

                javaMailSender.send(msg);
            } catch (Exception e) {
                log.warn("sendEmail error => ", e);
                throw new MailSenderException(e);
            }
        })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
