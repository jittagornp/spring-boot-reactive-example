package me.jittagornp.example.reactive;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.jittagornp.example.reactive.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public Mono<Message> getMessage(final ServerWebExchange exchange) {
        return Mono.fromCallable(() -> {
            return Message.builder()
                    .hello(messageService.getMessage(exchange, "hello", "jittagornp"))
                    .build();
        });
    }

    @Data
    @Builder
    public static class Message {

        private String hello;

    }

}
