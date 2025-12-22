package com.exchange.common.auth.core.config;

import com.exchange.common.auth.core.context.AuthProperties;
import com.exchange.common.auth.core.service.TokenService;
import com.exchange.common.auth.core.service.impl.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

@AutoConfiguration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthCoreAutoConfiguration {


    @Bean
    public TokenService tokenService(StringRedisTemplate redisTemplate,
                                     AuthProperties properties,
                                     ObjectMapper objectMapper) {
        return new JwtTokenService(redisTemplate, properties, objectMapper);
    }
}
