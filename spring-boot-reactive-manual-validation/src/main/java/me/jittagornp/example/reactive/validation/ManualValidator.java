/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.validation;

import java.util.List;

/**
 * @author jitta
 */
public interface ManualValidator {

    void validate(final Object object);

    void validate(final Object object, final List<Class<?>> groupClasses);

}
