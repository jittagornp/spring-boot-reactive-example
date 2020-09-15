/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.service;

import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Mono;
import java.util.List;

/**
 * @author jitta
 */
public interface FileService {

    Mono<List<String>> listAll();

    Mono<UploadResponse> upload(final byte[] bytes, final String fileName);

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
