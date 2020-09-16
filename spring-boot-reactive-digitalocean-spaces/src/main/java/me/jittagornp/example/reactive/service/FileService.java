/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.service;

import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.List;

/**
 * @author jitta
 */
public interface FileService {

    Mono<List<String>> listAll();

    Mono<UploadResponse> upload(final InputStream inputStream, final String fileName);

    Mono<Void> deleteByPathFile(final String pathFile);

    @Data
    @Builder
    public static class UploadResponse {

        private String displayName;

        private Long fileSize;

        private String contentType;

        private String message;

        private String path;

        private String url;

    }

}