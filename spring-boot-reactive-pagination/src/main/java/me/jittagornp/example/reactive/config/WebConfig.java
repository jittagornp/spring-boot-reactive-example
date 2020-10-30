/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.config;

import lombok.extern.slf4j.Slf4j;
import me.jittagornp.example.reactive.converter.QueryStringParameterToPaginationRequestConverter;
import me.jittagornp.example.reactive.converter.QueryStringParameterToPaginationRequestConverterImpl;
import me.jittagornp.example.reactive.model.PaginationRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Slf4j
@Configuration
public class WebConfig implements WebFluxConfigurer {

    @Override
    public void configureArgumentResolvers(final ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(new ReactivePaginationRequestMethodArgumentResolver());
    }

    public static class ReactivePaginationRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {

        private final QueryStringParameterToPaginationRequestConverter converter = new QueryStringParameterToPaginationRequestConverterImpl();

        @Override
        public boolean supportsParameter(final MethodParameter methodParameter) {
            return PaginationRequest.class.equals(methodParameter.getParameterType());
        }

        @Override
        public Mono<Object> resolveArgument(
                final MethodParameter methodParameter,
                final BindingContext bindingContext,
                final ServerWebExchange serverWebExchange
        ) {
            return Mono.just(converter.convert(serverWebExchange.getRequest().getQueryParams()));
        }
    }

}
