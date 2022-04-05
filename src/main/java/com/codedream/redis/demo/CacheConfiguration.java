package com.codedream.redis.demo;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Cache配置类
 */
@EnableCaching
@Configuration(proxyBeanMethods = false)
public class CacheConfiguration {

}
