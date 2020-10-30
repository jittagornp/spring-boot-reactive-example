/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.converter;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.jittagornp.example.reactive.exception.InvalidRequestException;
import me.jittagornp.example.reactive.model.PaginationRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import java.util.stream.Stream;
import static org.springframework.util.StringUtils.hasText;
import static java.util.stream.Collectors.toList;

/**
 * @author jitta
 */
@Slf4j
@Component
public class QueryStringParameterToPaginationRequestConverterImpl implements QueryStringParameterToPaginationRequestConverter {

    private static final int DEFAULT_LIMIT = 20;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_LIMIT = 100;
    private static final int MAX_SIZE = 100;

    @Override
    public PaginationRequest convert(final MultiValueMap<String, String> queryParams) {
        final long offset = parseLong(queryParams, "offset", 0L);
        final int limit = parseInt(queryParams, "limit", DEFAULT_LIMIT);
        final int page = parseInt(queryParams, "page", -1);
        final int size = parseInt(queryParams, "size", DEFAULT_SIZE);
        final Sort sort = convertToSort(queryParams.getFirst("sort"));
        return PaginationRequest.builder()
                .offset(offset)
                .limit(limit)
                .page(page)
                .size(size)
                .sort(sort)
                .defaultLimit(DEFAULT_LIMIT)
                .defaultSize(DEFAULT_SIZE)
                .maxLimit(MAX_LIMIT)
                .maxSize(MAX_SIZE)
                .build();
    }

    private int parseInt(final MultiValueMap<String, String> queryParams, final String paramName, final int defaultValue) {
        try {
            final String value = queryParams.getFirst(paramName);
            if (!hasText(value)) {
                return defaultValue;
            }
            return Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            throw new InvalidRequestException("Invalid pagination query \"" + paramName + "\"");
        }
    }

    private long parseLong(final MultiValueMap<String, String> queryParams, final String paramName, final long defaultValue) {
        try {
            final String value = queryParams.getFirst(paramName);
            if (!hasText(value)) {
                return defaultValue;
            }
            return Long.parseLong(value);
        } catch (final NumberFormatException e) {
            throw new InvalidRequestException("Invalid pagination query \"" + paramName + "\"");
        }
    }

    private Sort convertToSort(final String sort) {
        if (sort == null || sort.isEmpty()) {
            return Sort.unsorted();
        }
        final String[] arr = sort.split(",");
        if (arr == null || arr.length == 0) {
            throw new InvalidRequestException("Invalid pagination query \"sort\"");
        }
        return Sort.by(
                Stream.of(arr)
                        .map(item -> convertToSortOrder(item))
                        .filter(sortOrder -> hasText(sortOrder.getAttribute()))
                        .map(sortOrder -> new Sort.Order(sortOrder.getDirection(), sortOrder.getAttribute()))
                        .collect(toList())
        );
    }


    private String getSortAttribute(final String[] arr) {
        String attribute;
        try {
            attribute = arr[0];
        } catch (Exception e) {
            attribute = null;
        }
        if (attribute == null) {
            throw new InvalidRequestException("Invalid pagination query \"sort.attribute\"");
        }
        if (attribute.matches("\\d+")) {
            throw new InvalidRequestException("Invalid pagination query \"sort.attribute\" (" + attribute + ")");
        }
        return attribute;
    }

    private Sort.Direction getSortDirection(final String[] arr) {
        try {
            return Sort.Direction.valueOf(arr[1].toUpperCase());
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid pagination query \"sort.direction\"");
        }
    }

    private SortOrder convertToSortOrder(final String sort) {
        final String[] arr = sort.split(":");
        if (arr == null || arr.length == 0) {
            return SortOrder.builder().build();
        }
        final String attr = getSortAttribute(arr);
        if (attr == null || attr.isEmpty()) {
            return SortOrder.builder().build();
        }
        final Sort.Direction direction = getSortDirection(arr);
        return SortOrder.builder()
                .attribute(attr.trim())
                .direction(direction)
                .build();
    }

    @Data
    @Builder
    private static class SortOrder {

        private final String attribute;

        private final Sort.Direction direction;

    }


}
