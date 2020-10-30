/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author jitta
 */
@Data
@Builder
public class PaginationRequest {

    private long offset = 0;

    private int limit;

    private int page = -1;

    private int size;

    private Sort sort;

    private int defaultLimit = 20;

    private int defaultSize = 20;

    private int maxLimit = 100;

    private int maxSize = 100;

    public long getOffset() {
        if (offset < 0) {
            return 0;
        }
        return offset;
    }

    public int getLimit() {
        if (limit > maxLimit) {
            return maxLimit;
        }
        if (limit <= 0) {
            return defaultLimit;
        }
        return limit;
    }

    public int getPage() {
        if (page < 0) {
            return 0;
        }
        return page;
    }

    public int getSize() {
        if (size > maxSize) {
            return maxSize;
        }
        if (size <= 0) {
            return defaultSize;
        }
        return size;
    }

    public boolean isPageRequest() {
        return page >= 0;
    }

    public boolean isSliceRequest() {
        return !isPageRequest();
    }

    public Pageable toPage() {
        if (!isPageRequest()) {
            throw new IllegalArgumentException("It's not page request");
        }
        if (sort == null) {
            return PageRequest.of(getPage(), getSize());
        }
        return PageRequest.of(getPage(), getSize(), sort);
    }

    public Pageable toSlice() {
        if (!isSliceRequest()) {
            throw new IllegalArgumentException("It's not slice request");
        }
        if (sort == null) {
            return SliceRequest.of(getOffset(), getLimit());
        }
        return SliceRequest.of(getOffset(), getLimit(), sort);
    }

    public static class SliceRequest implements Pageable {

        private final long offset;

        private final int limit;

        private final Sort sort;

        protected SliceRequest(final long offset, final int limit, final Sort sort) {
            this.offset = offset;
            this.limit = limit;
            this.sort = sort;
        }

        public static SliceRequest of(final long offset, final int limit, final Sort sort) {
            return new SliceRequest(offset, limit, sort);
        }

        public static SliceRequest of(final long offset, final int limit) {
            return of(offset, limit, Sort.unsorted());
        }

        @Override
        public int getPageNumber() {
            return -1;
        }

        @Override
        public int getPageSize() {
            return limit;
        }

        @Override
        public long getOffset() {
            return offset;
        }

        @Override
        public Sort getSort() {
            return sort;
        }

        @Override
        public Pageable next() {
            return null;
        }

        @Override
        public Pageable previousOrFirst() {
            return null;
        }

        @Override
        public Pageable first() {
            return null;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }
    }
}
