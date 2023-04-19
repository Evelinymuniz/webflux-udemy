package br.com.udemy.webfluxudemy.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TrimStringValidator implements ConstraintValidator<TrimString, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.trim().length() == value.length();

        /*if (value == null){
            return true;
        }
        return value.trim().length() == value.length();*/
    }
}
