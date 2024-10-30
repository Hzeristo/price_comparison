package com.example.price_comparison.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{5,255}$";

    @Override
    public void initialize(ValidUsername constraintAnnotation) {}

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return username != null && username.matches(USERNAME_PATTERN) && username != "";
    }
}
