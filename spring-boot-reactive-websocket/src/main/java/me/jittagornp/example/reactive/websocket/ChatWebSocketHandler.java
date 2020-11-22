/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.channel.AbortedException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jitta
 */
@Slf4j
public class ChatWebSocketHandler implements WebSocketHandler {

    private final Map<String, WebSocketSession> sessionRepository = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> handle(final WebSocketSession wsSession) {
        return storeSession(wsSession)
                .flatMap(session -> {
                    return session.receive()
                            .flatMap(wsMessage -> {

                                final WebSocketMessage.Type type = wsMessage.getType();
                                final String message = wsMessage.getPayloadAsText();
                                log.debug("Received : id => \"{}\", type => \"{}\", message => \"{}\"", session.getId(), type, message);

                                return broadcastMessage(message);
                            }).then();
                });
    }

    private Mono<WebSocketSession> storeSession(final WebSocketSession session) {
        return Mono.fromCallable(() -> {
            //TODO
            log.debug("Session \"{}\" connected.", session.getId());
            sessionRepository.put(session.getId(), session);
            return session;
        });
    }

    private Mono<Void> removeSession(final WebSocketSession session) {
        return Mono.fromRunnable(() -> {
            //TODO
            log.debug("Session \"{}\" removed.", session.getId());
            sessionRepository.remove(session.getId());
        });
    }

    private Mono<Void> broadcastMessage(final String message) {
        log.debug("Broadcast message to {} clients", sessionRepository.size());
        return Flux.fromIterable(sessionRepository.entrySet())
                .map(entry -> entry.getValue())
                .flatMap(session -> {
                    log.debug("Send message to client \"{}\"", session.getId());
                    return session.send(Mono.just(session.textMessage(message)))
                            .onErrorResume(AbortedException.class, e -> {
                                log.debug("Error => \"{}\"", e.getMessage());
                                return removeSession(session);
                            });
                }).then();
    }
}
