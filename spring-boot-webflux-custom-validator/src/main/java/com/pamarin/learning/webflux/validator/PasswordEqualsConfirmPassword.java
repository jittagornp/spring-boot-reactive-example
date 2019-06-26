/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordEqualsConfirmPassword.Validator.class)
public @interface PasswordEqualsConfirmPassword {

    String message() default "password not equals confirm password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Component
    public static class Validator implements ConstraintValidator<PasswordEqualsConfirmPassword, Model> {

        private PasswordEqualsConfirmPassword annotation;

        @Override
        public void initialize(PasswordEqualsConfirmPassword annotation) {
            this.annotation = annotation;
        }

        @Override
        public boolean isValid(Model model, ConstraintValidatorContext context) {
            String password = model.getPassword();
            String confirmPassword = model.getConfirmPassword();
            if (password == null) {
                password = "";
            }
            if (confirmPassword == null) {
                confirmPassword = "";
            }
            return FieldNameModifier.of(context)
                    .withMessage(annotation.message())
                    .changeTo("confirmPassword")
                    .valid(password.equals(confirmPassword));
        }

        public boolean isValid(Model model) {
            return isValid(model, null);
        }

    }

    public static interface Model {

        String getPassword();

        String getConfirmPassword();

    }

}
