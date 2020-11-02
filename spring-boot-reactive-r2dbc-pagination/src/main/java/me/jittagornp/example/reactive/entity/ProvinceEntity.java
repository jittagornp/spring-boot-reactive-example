/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.util.UUID;

/**
 * @author jitta
 */
@Data
@Builder
@Table("app.province")
public class ProvinceEntity {

    //Primary Key
    @Id
    private UUID id;

    private String name;
}
