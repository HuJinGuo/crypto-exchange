package com.exchange.common.redis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        RedisTemplateConfig.class,
        RedissonConfig.class
})
public class RedisAutoConfiguration {
}