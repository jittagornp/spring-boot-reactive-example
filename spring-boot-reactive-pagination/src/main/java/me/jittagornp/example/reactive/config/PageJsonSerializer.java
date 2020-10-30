/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.data.domain.Page;

/**
 * @author jitta
 */
public class PageJsonSerializer extends JsonSerializer<Page> {

    @Override
    public void serialize(
            final Page page,
            final JsonGenerator generator,
            final SerializerProvider serializerProvider
    ) throws IOException {
        generator.writeStartObject();
        generator.writeObjectField("elements", page.getContent());
        generator.writeObjectField("elements_size", page.getNumberOfElements());
        generator.writeObjectField("page", page.getNumber());
        generator.writeObjectField("size", page.getSize());
        generator.writeObjectField("total", page.getTotalPages());
        generator.writeObjectField("total_elements", page.getTotalElements());
        generator.writeObjectField("has_elements", page.hasContent());
        generator.writeObjectField("has_previous", page.hasPrevious());
        generator.writeObjectField("has_next", page.hasNext());
        generator.writeObjectField("is_first", page.isFirst());
        generator.writeObjectField("is_last", page.isLast());
        generator.writeObjectField("first", 0);
        generator.writeObjectField("last", (page.getTotalPages() == 0) ? 0 : (page.getTotalPages() - 1));
        generator.writeEndObject();
    }

}
