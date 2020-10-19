/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import lombok.RequiredArgsConstructor;
import me.jittagornp.example.reactive.service.MailSenderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class TestMailController {

    private final MailSenderService mailSenderService;

    @GetMapping("/send")
    public Mono<Void> sendMail(){
        return mailSenderService.sendEmail(
                MailSenderService.Message.builder()
                        .subject("Test send email")
                        .to("jittagornp@gmail.com")
                        .body("Hello jittagornp")
                        .build()
        );
    }

}
