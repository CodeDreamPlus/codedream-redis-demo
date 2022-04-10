package com.codedreamplus.redis.demo.controller;

import com.codedreamplus.redis.CodeDreamPlusRedis;
import com.codedreamplus.redis.demo.service.DemoService;
import com.codedreamplus.redis.lock.LockType;
import com.codedreamplus.redis.lock.RedisLock;
import com.codedreamplus.redis.lock.RedisLockClient;
import com.codedreamplus.redis.ratelimiter.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 演示控制器
 *
 * @author cool
 * @date 2022/04/10
 */
@RestController
@AllArgsConstructor
public class DemoController {

    private final CodeDreamPlusRedis codeDreamPlusRedis;
    private final RedisLockClient redisLockClient;
    private final DemoService demoService;

    @GetMapping
    //限流，每分钟只能获取五次
    @RateLimiter(value = "test", max = 5, ttl = 1, timeUnit = TimeUnit.MINUTES)
    public String get() {
        return codeDreamPlusRedis.get("test");
    }


    @PostMapping
    public Boolean save(@RequestParam String value) {
        codeDreamPlusRedis.set("test", value);
        return Boolean.TRUE;
    }


    @GetMapping("/reentrantLock")
    @RedisLock(lockName = "testLock", waitTime = 10, leaseTime = 60, type = LockType.REENTRANT)
    public String getReentrantLock() {
        return getReentrantLockMethod();
    }


    @RedisLock(lockName = "testLock", waitTime = 10, leaseTime = 60, type = LockType.REENTRANT)
    public String getReentrantLockMethod() {
        return codeDreamPlusRedis.get("test");
    }

    /**
     * 自定义锁
     *
     * @return {@link String}
     */
    @GetMapping("/customLock")
    public String customLock() {
        return redisLockClient.lock("customLock", LockType.REENTRANT, 5, 10, TimeUnit.SECONDS, demoService);
    }


    /**
     * 缓存
     *
     * @return {@link String}
     */
    @GetMapping("/cacheable")
    @Cacheable(cacheNames = "testCacheable#60")
    public String cacheable() {
        return codeDreamPlusRedis.get("test");
    }
}
