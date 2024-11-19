package com.haydenshui.pricecomparison.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisDistributedLock {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisDistributedLock(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取分布式锁
     * @param lockKey 锁的键
     * @param timeout 锁的超时时间（单位：秒）
     * @return 锁是否获取成功
     */
    public boolean lock(String lockKey, long timeout) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        Boolean success = valueOps.setIfAbsent(lockKey, "LOCKED", timeout, TimeUnit.SECONDS);
        return success != null && success;
    }

    /**
     * 释放分布式锁
     * @param lockKey 锁的键
     */
    public void unlock(String lockKey) {
        redisTemplate.delete(lockKey);
    }
}
