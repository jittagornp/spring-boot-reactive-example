package me.jittagornp.example.reactive.service;

import org.springframework.web.server.ServerWebExchange;
import java.util.Locale;

public interface MessageService {

    String getMessage(final ServerWebExchange exchange, final String propertyName, final Object...params);

    String getMessage(final Locale locale, final String propertyName, final Object...params);

}
