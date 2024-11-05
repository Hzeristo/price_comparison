package com.example.price_comparison.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidName, String> {

    private static final String NAME_PATTERN = "^[\\p{L}\\p{N}_\\p{P}\\u4e00-\\u9fa5]{5,255}$";

    @Override
    public void initialize(ValidName constraintAnnotation) {}

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        boolean valid = name.matches(NAME_PATTERN);
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Name must be between 8 and 255 characters long and contain at least one uppercase letter, one lowercase letter, and one digit.").addConstraintViolation();
        }
        return valid;
    }
}
