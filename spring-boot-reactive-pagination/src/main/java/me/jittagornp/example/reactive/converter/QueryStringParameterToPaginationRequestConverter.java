package me.jittagornp.example.reactive.converter;

import me.jittagornp.example.reactive.model.PaginationRequest;
import org.springframework.util.MultiValueMap;

public interface QueryStringParameterToPaginationRequestConverter {

    PaginationRequest convert(final MultiValueMap<String, String> queryParams);

}
