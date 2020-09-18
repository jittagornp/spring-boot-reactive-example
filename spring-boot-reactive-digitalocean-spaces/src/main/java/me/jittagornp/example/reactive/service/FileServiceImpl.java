/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import me.jittagornp.example.reactive.config.DigitalOceanSpacesProperties;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.io.InputStream;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 * @author jitta
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final DigitalOceanSpacesProperties properties;

    private final AmazonS3 amazonS3;

    @Override
    public Mono<List<String>> listAll() {
        return Mono.fromCallable(() -> {
            final ObjectListing objectListing = amazonS3.listObjects(properties.getBucketName());
            return objectListing.getObjectSummaries()
                    .stream()
                    .map(S3ObjectSummary::getKey)
                    .collect(toList());
        })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UploadResponse> upload(final InputStream inputStream, final String fileName) {
        return Mono.fromCallable(() -> {
            try {
                final String folder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                final String key = folder + "/" + fileName;
                final ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(URLConnection.guessContentTypeFromName(fileName));
                final PutObjectRequest request = new PutObjectRequest(properties.getBucketName(), key, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);
                final PutObjectResult result = amazonS3.putObject(request);
                return mapResponse(result, key);
            } finally {
                inputStream.close();
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private UploadResponse mapResponse(final PutObjectResult result, final String key) {
        return UploadResponse.builder()
                .displayName(key)
                .contentType(result.getMetadata().getContentType())
                .fileSize(result.getMetadata().getContentLength())
                .message("OK upload file \"" + key + "\" to DigitalOcean Spaces success")
                .path("/" + key)
                .url(buildAccessUrl(key))
                .build();
    }

    private String buildAccessUrl(final String key) {
        return new StringBuilder()
                .append("https://")
                .append(properties.getBucketName())
                .append(".")
                .append(properties.getEndpoint())
                .append("/")
                .append(key)
                .toString();
    }

    @Override
    public Mono<Void> deleteByPathFile(final String pathFile) {
        return Mono.fromRunnable(() -> {
            final DeleteObjectRequest request = new DeleteObjectRequest(properties.getBucketName(), pathFile);
            amazonS3.deleteObject(request);
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}