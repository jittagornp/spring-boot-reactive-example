/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author jitta
 */
@Slf4j
@RestController
public class UploadController {

    private static final String UPLOAD_DIRECTORY = "/temp";

    @GetMapping({"", "/"})
    public Mono<String> hello() {
        return Mono.just("Hello world.");
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Map> upload(
            @RequestPart("file") final FilePart filePart,
            final FormData formData
    ) {
        log.debug("formData => {}", formData);
        
        final File directory = new File(UPLOAD_DIRECTORY);
        if(!directory.exists()){
            directory.mkdirs();
        }

        final File file = new File(directory, filePart.filename());

        return filePart
                .transferTo(file)
                .then(Mono.fromCallable(() -> {
                    final Map<String, Object> map = new HashMap<>();
                    map.put("name", file.getName());
                    map.put("lastModified", file.lastModified());
                    map.put("size", file.length());
                    return map;
                }));
    }

    @Data
    public static class FormData {

        private String description;

    }
}
