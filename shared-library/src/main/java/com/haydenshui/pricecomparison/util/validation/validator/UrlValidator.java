package com.haydenshui.pricecomparison.shared.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class UrlValidator implements ConstraintValidator<ValidUrl, String> {

    private static final String URL_PATTERN = "^(http|https)://.*$"; // 简单的URL正则

    @Override
    public void initialize(ValidUrl constraintAnnotation) {}

    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        if (url == null || url.isEmpty()) {
            return false; // 如果URL为null或空字符串，返回false
        }

        boolean valid = Pattern.matches(URL_PATTERN, url);
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("URL must start with http:// or https://").addConstraintViolation();
        }
        return valid;
    }
}
