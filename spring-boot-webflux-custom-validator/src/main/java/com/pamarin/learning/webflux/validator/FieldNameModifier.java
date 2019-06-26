/*
 * Copy right 2016 - 2017 Genius-Tree Co., Ltd.
 */
package com.pamarin.learning.webflux.validator;

import javax.validation.ConstraintValidatorContext;

/**
 * @author jittagorn
 * create : 2017/03/12
 */
public class FieldNameModifier {

    private final ConstraintValidatorContext context;
    private String fieldName;
    private String message;

    private FieldNameModifier(ConstraintValidatorContext context) {
        this.context = context;
    }

    public static FieldNameModifier of(ConstraintValidatorContext context) {
        return new FieldNameModifier(context);
    }

    public FieldNameModifier changeTo(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public FieldNameModifier withMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean valid(boolean isValid) {
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
