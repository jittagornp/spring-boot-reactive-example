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
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordNotEqualsEmail.Validator.class)
public @interface PasswordNotEqualsEmail {

    String message() default "password equals email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    @Component
    public static class Validator implements ConstraintValidator<PasswordNotEqualsEmail, PasswordNotEqualsEmail.Model> {

        private PasswordNotEqualsEmail annotation;

        @Override
        public void initialize(PasswordNotEqualsEmail annotation) {
            this.annotation = annotation;
        }

        @Override
        public boolean isValid(PasswordNotEqualsEmail.Model model, ConstraintValidatorContext context) {
            String email = model.getEmail();
            String password = model.getPassword();
            if (!(hasText(email) && hasText(password))) {
                return true;
            }
            return FieldNameModifier.of(context)
                    .withMessage(annotation.message())
                    .changeTo("password")
                    .valid(!email.equals(password));
        }

        public boolean isValid(PasswordNotEqualsEmail.Model model) {
            return isValid(model, null);
        }

    }

    public static interface Model {

        String getEmail();

        String getPassword();

    }
}
