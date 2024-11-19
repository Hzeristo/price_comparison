package com.haydenshui.pricecomparison.user;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {
    /**
     * 锁的键，可以支持动态表达式（例如 SpEL）
     */
    String key();

    /**
     * 锁的超时时间，默认5秒
     */
    long timeout() default 5;
}
