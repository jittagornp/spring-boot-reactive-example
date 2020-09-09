package me.jittagornp.example.reactive.handler;

import me.jittagornp.example.reactive.model.ErrorResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface ErrorResponseProducer {

    Mono<Void> produce(final ErrorResponse err, final ServerWebExchange exchange);

}
