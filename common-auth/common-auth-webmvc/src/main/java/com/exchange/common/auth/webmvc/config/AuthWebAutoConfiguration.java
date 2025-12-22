package com.exchange.common.auth.webmvc.config;

import com.exchange.common.auth.core.context.AuthProperties;
import com.exchange.common.auth.core.service.TokenService;
import com.exchange.common.auth.webmvc.filter.ReactiveAuthWebFilter;
import com.exchange.common.auth.webmvc.filter.ServletAuthFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.WebFilter;

@AutoConfiguration(
        after = com.exchange.common.auth.core.config.AuthCoreAutoConfiguration.class // ✅ 强制在 core 之后加载
)
@ConditionalOnBean(TokenService.class) // 只有当 TokenService 存在时才注册 filter
@EnableConfigurationProperties(AuthProperties.class)
public class AuthWebAutoConfiguration {

//    /**
//     * Gateway（WebFlux + spring-cloud-gateway 在 classpath 才生效）
//     */
//    @Bean
//    @ConditionalOnClass(name = "org.springframework.cloud.gateway.filter.GlobalFilter")
//    @ConditionalOnMissingBean(GlobalFilter.class)
//    public GatewayAuthGlobalFilter gatewayAuthGlobalFilter(TokenService tokenService, AuthProperties properties) {
//        return new GatewayAuthGlobalFilter(tokenService, properties);
//    }

    /**
     * 普通 Servlet 服务（spring-web 在 classpath 才生效）
     */
    @Bean
    @ConditionalOnClass(name = "jakarta.servlet.Filter")
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnMissingBean(OncePerRequestFilter.class)
    public ServletAuthFilter servletAuthFilter(TokenService tokenService, AuthProperties properties) {
        return new ServletAuthFilter(tokenService, properties);
    }


    /**
     * WebFlux 服务（非网关的 reactive 服务）
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @ConditionalOnClass(name = "org.springframework.web.server.WebFilter")
    @ConditionalOnMissingBean(WebFilter.class)
    public ReactiveAuthWebFilter reactiveAuthWebFilter(TokenService tokenService, AuthProperties properties) {
        return new ReactiveAuthWebFilter(tokenService, properties);
    }
}