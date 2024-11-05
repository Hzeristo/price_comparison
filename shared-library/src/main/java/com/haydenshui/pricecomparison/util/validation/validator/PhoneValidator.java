package com.haydenshui.pricecomparison.shared.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final String PHONE_PATTERN = "^\\d{13}$"; // 13位数字的正则表达式

    @Override
    public void initialize(ValidPhone constraintAnnotation) {}

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isEmpty()) {
            return false; // 如果手机号为null或空字符串，返回false
        }

        boolean valid = phone.matches(PHONE_PATTERN);
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Phone number must be exactly 13 digits.").addConstraintViolation();
        }
        return valid;
    }
}
