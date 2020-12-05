/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.config;

import me.jittagornp.example.reactive.websocket.SimpleChatWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jitta
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public HandlerMapping handlerMapping() {
        final Map<String, WebSocketHandler> urlMap = new HashMap<>();
        urlMap.put("/chat", new SimpleChatWebSocketHandler());
        final int order = -1; // before annotated controllers
        return new SimpleUrlHandlerMapping(urlMap, order);
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

}
