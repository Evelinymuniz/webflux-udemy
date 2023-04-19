package br.com.udemy.webfluxudemy.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.FIELD;

@Constraint(validatedBy = { TrimStringValidator.class })
@Target({ FIELD })
@Retention(RUNTIME)
public @interface TrimString {

    String message() default "field connot have blank spaces at the beginning or at end";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}