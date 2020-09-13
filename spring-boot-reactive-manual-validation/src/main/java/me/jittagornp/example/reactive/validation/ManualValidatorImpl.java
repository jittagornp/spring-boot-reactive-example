/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.validation;

import javax.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jitta
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ManualValidatorImpl implements ManualValidator {

    private final Validator validator;

    public void validate(final Object object) {
        validate(object, null);
    }

    public void validate(final Object object, final List<Class<?>> groupClasses) {
        final BindingResult bindingResult = new BindException(object, "");
        final WebExchangeBindException e = new WebExchangeBindException(fakeMethodParameter(), bindingResult);
        final List<Class<?>> classes = new ArrayList<>();
        if (groupClasses != null) {
            classes.addAll(groupClasses);
        }
        classes.add(Default.class);
        ValidationUtils.invokeValidator(validator, object, e, classes);
        if (e.hasErrors()) {
            throw e;
        }
    }

    private MethodParameter fakeMethodParameter(){
        try {
            final Method method = getClass().getDeclaredMethod("fakeMethodParameter");
            return new MethodParameter(method, -1);
        } catch (final Exception e) {
            log.warn("fakeMethodParameter error => ", e);
            return null;
        }
    }

}