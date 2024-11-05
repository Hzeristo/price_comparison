package com.example.price_comparison.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{5,255}$";

    @Override
    public void initialize(ValidUsername constraintAnnotation) {}

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.isEmpty()) {
            return false; // 如果用户名为null或空字符串，返回false
        }

        boolean valid = username.matches(USERNAME_PATTERN);
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Username must be between 5 and 255 characters long and can only contain letters, numbers, and underscores.").addConstraintViolation();
        }
        return valid;
    }
}
