package me.jittagornp.example.reactive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageSource messageSource;

    private final LocaleContextResolver localeContextResolver;

    @Override
    public String getMessage(final ServerWebExchange exchange, final String propertyName, final Object... params) {
        final LocaleContext localeContext = localeContextResolver.resolveLocaleContext(exchange);
        final Locale locale = localeContext.getLocale();
        return getMessage(locale, propertyName, params);
    }

    @Override
    public String getMessage(final Locale locale, final String propertyName, final Object... params) {
        return messageSource.getMessage(propertyName, params, locale);
    }
}
