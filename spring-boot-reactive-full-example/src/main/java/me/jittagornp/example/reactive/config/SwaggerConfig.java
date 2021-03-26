/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.config;

import java.util.Arrays;
import java.util.Collections;
import static java.util.Collections.singletonList;
import java.util.List;
import static java.util.stream.Collectors.toSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jittagornp.example.reactive.model.PaginationRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

/**
 * @author jitta
 */
@Slf4j
@Configuration
@EnableSwagger2WebFlux
@RequiredArgsConstructor
public class SwaggerConfig {

    private static final String SECURITY_KEY_NAME = "Bearer";

    private final Class[] ignoredParameterTypes = {
            ServerWebExchange.class,
            Resource.class,
            ResponseEntity.class,
            Mono.class,
            Flux.class
    };

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .build()
                .genericModelSubstitutes(Mono.class, Flux.class)
                .useDefaultResponseMessages(false)
                .produces(Arrays.asList("application/json;charset=UTF-8").stream().collect(toSet()))
                .securitySchemes(singletonList(securityScheme()))
                .securityContexts(singletonList(securityContext()))
                .ignoredParameterTypes(ignoredParameterTypes)
                .directModelSubstitute(PaginationRequest.class, PaginationRequest.SwaggerModel.class)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        final String title = "Example API";
        final String description = "ตัวอย่างการ Config Spring-boot Reactive Swagger";
        final String version = "1.0";
        final String termOfServiceUrl = "https://www.jittagornp.me/term-of-service";
        final String contactName = "จิตกร พิทักษ์เมธากุล";
        final String contactUrl = "https://www.jittagornp.me/contact";
        final String contactEmail = "jittagornp@gmail.com";
        final String license = "ไม่อนุญาตให้นำส่วนหนึ่งส่วนใดของ API ไปใช้งาน ยกเว้นจะได้รับอนุญาตจากเจ้าของผลงาน";
        final String licenseUrl = "https://www.jittagornp.me/license";
        return new ApiInfo(
                title,
                description,
                version,
                termOfServiceUrl,
                new springfox.documentation.service.Contact(contactName, contactUrl, contactEmail),
                license,
                licenseUrl,
                Collections.emptyList()
        );
    }

    private SecurityScheme securityScheme() {
        return new ApiKey(SECURITY_KEY_NAME, "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(path -> !(path.startsWith("/auth/") || path.startsWith("/public/")))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        return singletonList(new SecurityReference(SECURITY_KEY_NAME, new AuthorizationScope[]{
                new AuthorizationScope("global", "accessEverything")
        }));
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder() //<20>
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.MODEL)
                .displayRequestDuration(false)
                .docExpansion(DocExpansion.LIST)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .showCommonExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                .validatorUrl(null)
                .build();
    }
}