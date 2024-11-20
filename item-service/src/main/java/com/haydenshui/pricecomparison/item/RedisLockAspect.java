package com.haydenshui.pricecomparison.item;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RedisLockAspect {

    private final RedisDistributedLock redisDistributedLock;

    @Autowired
    public RedisLockAspect(RedisDistributedLock redisDistributedLock) {
        this.redisDistributedLock = redisDistributedLock;
    }

    @Around("@annotation(redisLock)")
    public Object handleRedisLock(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        String lockKey = redisLock.key();
        long timeout = redisLock.timeout();
        if (redisDistributedLock.lock(lockKey, timeout)) {
            try {
                return joinPoint.proceed();
            } finally {
                redisDistributedLock.unlock(lockKey);
            }
        } else {
            throw new RuntimeException("Failed to acquire lock for key: " + lockKey);
        }
    }
}
