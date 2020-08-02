/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import me.jittagornp.example.reactive.exception.NotFoundException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

/**
 * @author jitta
 */
@Slf4j
@Controller
public class ResourceController {

    @GetMapping({"", "/", "/classpath"})
    public Mono<ResponseEntity<Resource>> showClassPathImage() {
        return Mono.just(
                ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"reactive_spring.png\"")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                        .body(new ClassPathResource("static/image/reactive_spring.png"))
        );
    }

    @GetMapping({"/classpath/download"})
    public Mono<ResponseEntity<Resource>> downloadClassPathImage() {
        return Mono.just(
                ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reactive_spring.png\"")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                        .body(new ClassPathResource("static/image/reactive_spring.png"))
        );
    }

    @GetMapping("/inputstream")
    public Mono<ResponseEntity<? extends Resource>> showInputStreamImage() {
        String fileName = "reactive_spring.png";
        return readStream(fileName)
                .map(stream -> {
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                            .body(new InputStreamResource(stream));
                });
    }

    @GetMapping("/inputstream/download")
    public Mono<ResponseEntity<Resource>> downloadInputStreamImage() {
        String fileName = "reactive_spring.png";
        return readStream(fileName)
                .map(stream -> {
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                            .body(new InputStreamResource(stream));
                });
    }

    private Mono<InputStream> readStream(final String fileName) {
        return Mono
                .create((MonoSink<InputStream> callback) -> {
                    try {
                        callback.success(getClass().getResourceAsStream("/static/image/" + fileName));
                    } catch (Exception ex) {
                        log.warn("read stream error => ", ex);
                        callback.error(ex);
                    }
                })
                .switchIfEmpty(Mono.error(new NotFoundException(fileName + " not found")));
    }
}
