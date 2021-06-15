/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.mapper;

import me.jittagornp.example.reactive.entity.UserEntity;
import me.jittagornp.example.reactive.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.*;

/**
 * @author jitta
 */
@Mapper
public interface UserMapper {

    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "epochMilliToOffsetDateTime")
    User map(final UserEntity entity);

    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "offsetDateTimeToEpochMilli")
    UserEntity map(final User user);

    @Named("offsetDateTimeToEpochMilli")
    default Long offsetDateTimeToEpochMilli(final OffsetDateTime offsetDateTime) {
        try {
            return offsetDateTime.toInstant().toEpochMilli();
        } catch (Exception e) {
            return null;
        }
    }

    @Named("epochMilliToOffsetDateTime")
    default OffsetDateTime epochMilliToOffsetDateTime(final Long epochMilli) {
        try {
            final Instant instant = Instant.ofEpochMilli(epochMilli);
            return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
        } catch (Exception e) {
            return null;
        }
    }

}
