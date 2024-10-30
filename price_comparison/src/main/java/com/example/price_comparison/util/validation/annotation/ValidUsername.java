package com.example.price_comparison.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class) // 指定验证器类
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    String message() default "Invalid username format"; // 默认错误消息

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
