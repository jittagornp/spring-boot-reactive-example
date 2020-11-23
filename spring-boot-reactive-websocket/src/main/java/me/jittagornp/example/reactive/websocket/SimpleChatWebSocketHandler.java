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
public class SimpleChatWebSocketHandler implements WebSocketHandler {

    private final Map<String, WebSocketSession> sessionRepository = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> handle(final WebSocketSession newSession) {
        return storeSession(newSession)
                .flatMap(session -> {
                    return session.receive()
                            .flatMap(wsMessage -> {

                                final WebSocketMessage.Type type = wsMessage.getType();
                                final String message = wsMessage.getPayloadAsText();
                                log.debug("Received : id => \"{}\", type => \"{}\", message => \"{}\"", session.getId(), type, message);

                                return broadcastMessage(session, message);
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

    private Flux<WebSocketSession> findAllSessions() {
        return Flux.fromIterable(sessionRepository.entrySet())
                .map(entry -> entry.getValue());
    }

    private Mono<Void> broadcastMessage(final WebSocketSession sourceSession, final String originMessage) {
        log.debug("Broadcast message to {} clients", sessionRepository.size());
        return findAllSessions()
                .flatMap(session -> {
                    log.debug("Send message to client \"{}\"", session.getId());
                    final String message = sourceSession.getId() + " : " + originMessage;
                    return session.send(Mono.just(session.textMessage(message)))
                            .onErrorResume(AbortedException.class, e -> {
                                log.debug("Error => \"{}\"", e.getMessage());
                                return removeSession(session);
                            });
                }).then();
    }
}
