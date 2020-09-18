/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.service;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author jitta
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private static final String BUCKET_NAME = "jittagornp-example";

    private static final String PUBLIC_URL = "https://storage.googleapis.com/" + BUCKET_NAME + "/%s";

    //Google Cloud Storage
    private final Storage storage;

    @Override
    public Mono<List<String>> listAll() {
        return Mono.fromCallable(() -> {
            final List<String> output = new ArrayList<>();
            Page<Blob> page = storage.list(BUCKET_NAME);
            while (true) {
                final Iterable<Blob> values = page.getValues();
                final Iterator<Blob> iterator = values.iterator();
                while (iterator.hasNext()) {
                    final Blob blob = iterator.next();
                    output.add(blob.getName());
                }

                if (!page.hasNextPage()) {
                    break;
                }

                page = page.getNextPage();
            }
            return output;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UploadResponse> upload(final byte[] bytes, final String fileName) {
        return Mono.fromCallable(() -> {
            final String folder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            final String blobName = folder + "/" + fileName;
            final Acl allUsersCanRead = Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER);
            final BlobInfo blobInfo = BlobInfo.newBuilder(BUCKET_NAME, blobName)
                    .setAcl(Arrays.asList(allUsersCanRead))
                    .setContentType(URLConnection.guessContentTypeFromName(fileName))
                    .setContentDisposition("inline; filename=\"" + fileName + "\"")
                    .build();
            final Blob blob = storage.create(blobInfo, bytes);
            return mapResponse(blob, fileName);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private UploadResponse mapResponse(final Blob blob, final String fileName) {
        return UploadResponse.builder()
                .displayName(fileName)
                .contentType(blob.getContentType())
                .fileSize(blob.getSize())
                .message("OK upload file \"" + blob.getName() + "\" to Google Cloud Storage success")
                .path("/" + blob.getName())
                .url(String.format(PUBLIC_URL, blob.getName()))
                .build();
    }
}
