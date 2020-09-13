/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.validator;

import javax.validation.ConstraintValidatorContext;

/**
 * @author jitta
 */
public class FieldNameModifier {

    private final ConstraintValidatorContext context;

    private String fieldName;

    private String message;

    private FieldNameModifier(final ConstraintValidatorContext context) {
        this.context = context;
    }

    public static FieldNameModifier of(final ConstraintValidatorContext context) {
        return new FieldNameModifier(context);
    }

    public FieldNameModifier changeTo(final String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public FieldNameModifier withMessage(final String message) {
        this.message = message;
        return this;
    }

    public boolean valid(final boolean isValid) {
        if (isValid) {
            return true;
        }

        if (context != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.message)
                    .addPropertyNode(this.fieldName)
                    .addConstraintViolation();
        }

        return false;
    }

}
