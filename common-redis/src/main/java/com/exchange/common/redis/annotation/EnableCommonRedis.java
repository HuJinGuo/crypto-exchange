package com.exchange.common.redis.annotation;


import com.exchange.common.redis.config.RedisTemplateConfig;
import com.exchange.common.redis.config.RedissonConfig;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({
        RedisTemplateConfig.class,
        RedissonConfig.class
})
public @interface EnableCommonRedis {
}
