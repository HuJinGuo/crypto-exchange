package com.exchange.common.auth.webflux.config;

import com.exchange.common.auth.core.context.AuthProperties;
import com.exchange.common.auth.core.service.TokenService;
import com.exchange.common.auth.webflux.filter.GatewayAuthGlobalFilter;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@AutoConfiguration(
        after = com.exchange.common.auth.core.config.AuthCoreAutoConfiguration.class
)
@ConditionalOnBean(TokenService.class)
@EnableConfigurationProperties(AuthProperties.class)
public class AuthFluxAutoConfiguration {

    @PostConstruct
    public void init() {
        log.info(">>> AuthFluxAutoConfiguration loaded");
    }

    @Bean
    // 注意：移除了 @ConditionalOnMissingBean(GlobalFilter.class)
    @ConditionalOnClass(name = "org.springframework.cloud.gateway.filter.GlobalFilter")
    public GatewayAuthGlobalFilter gatewayAuthGlobalFilter(TokenService tokenService, AuthProperties properties) {
        return new GatewayAuthGlobalFilter(tokenService, properties);
    }
}