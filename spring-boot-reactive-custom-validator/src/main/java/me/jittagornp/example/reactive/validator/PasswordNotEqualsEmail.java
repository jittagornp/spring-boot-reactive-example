/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.validator;

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
 * @author jitta
 */
@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordNotEqualsEmail.Validator.class)
public @interface PasswordNotEqualsEmail {

    String message() default "Password equals Email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    @Component
    public static class Validator implements ConstraintValidator<PasswordNotEqualsEmail, PasswordNotEqualsEmail.Model> {

        private PasswordNotEqualsEmail annotation;

        @Override
        public void initialize(final PasswordNotEqualsEmail annotation) {
            this.annotation = annotation;
        }

        @Override
        public boolean isValid(final PasswordNotEqualsEmail.Model model, final ConstraintValidatorContext context) {
            final String email = model.getEmail();
            final String password = model.getPassword();
            if (!(hasText(email) && hasText(password))) {
                return true;
            }
            return FieldNameModifier.of(context)
                    .withMessage(annotation.message())
                    .changeTo("password")
                    .valid(!email.equals(password));
        }

    }

    public static interface Model {

        String getEmail();

        String getPassword();

    }
}
