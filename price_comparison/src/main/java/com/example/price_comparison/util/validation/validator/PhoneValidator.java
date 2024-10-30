package com.example.price_comparison.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final String PHONE_PATTERN = "^\\d{13}$"; // Adjust for your needs

    @Override
    public void initialize(ValidPhone constraintAnnotation) {}

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return phone != null && phone.matches(PHONE_PATTERN);
    }
}