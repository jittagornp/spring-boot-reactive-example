package me.jittagornp.example.reactive.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Pagination<T> {

    @JsonProperty(value = "elements")
    private List<T> elements;

    @JsonProperty(value = "elements_size")
    private int elementsSize;

    @JsonProperty(value = "page")
    private int page;

    @JsonProperty(value = "size")
    private int size;

    @JsonProperty(value = "total")
    private int total;

    @JsonProperty(value = "total_elements")
    private int totalElements;

    @JsonProperty(value = "has_elements")
    private boolean hasElements;

    @JsonProperty(value = "has_previous")
    private boolean hasPrevious;

    @JsonProperty(value = "has_next")
    private boolean hasNext;

    @JsonProperty(value = "is_first")
    private boolean isFirst;

    @JsonProperty(value = "is_last")
    private boolean isLast;

    @JsonProperty(value = "first")
    private int first;

    @JsonProperty(value = "last")
    private int last;
}
