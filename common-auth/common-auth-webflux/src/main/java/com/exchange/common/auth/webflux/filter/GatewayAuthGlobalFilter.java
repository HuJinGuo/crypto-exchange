package com.exchange.common.auth.webflux.filter;


import com.exchange.common.auth.core.constant.AuthConstants;
import com.exchange.common.auth.core.context.AuthProperties;
import com.exchange.common.auth.core.context.UserContext;
import com.exchange.common.auth.core.service.TokenService;
import com.exchange.common.auth.core.utils.AuthHeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class GatewayAuthGlobalFilter implements GlobalFilter, Ordered {

    private final TokenService tokenService;
    private final AuthProperties properties;
    private final AntPathMatcher matcher = new AntPathMatcher();

    public GatewayAuthGlobalFilter(TokenService tokenService, AuthProperties properties) {
        this.tokenService = tokenService;
        this.properties = properties;
    }

    @Override
    public int getOrder() {
        return -100;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        log.info("[AUTH-GW] request path={}", path);

        // 1️⃣ 白名单直接放行
        if (isWhitelisted(path)) {
            log.info("[AUTH-GW] whitelist path, skip auth");
            return chain.filter(exchange);
        }

        // 2️⃣ 读取 Authorization
        String auth = exchange.getRequest()
                .getHeaders()
                .getFirst(AuthConstants.AUTH_HEADER);

        String token = AuthHeaderUtil.extractBearerToken(auth);

        // 3️⃣ 没 token —— 直接拦截（你之前缺的就是这里）
        if (!StringUtils.hasText(token)) {
            log.warn("[AUTH-GW] missing token, path={}", path);
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 4️⃣ 校验 token
        UserContext ctx;
        try {
            ctx = tokenService.parseToken(token);
        } catch (Exception e) {
            log.warn("[AUTH-GW] invalid token, path={}, msg={}", path, e.getMessage());
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 5️⃣ 注入用户信息
        ServerHttpRequest newReq = exchange.getRequest().mutate()
                .header(AuthConstants.HEADER_USER_ID, String.valueOf(ctx.getUserId()))
                .header(AuthConstants.HEADER_USERNAME,
                        ctx.getUserName() == null ? "" : ctx.getUserName())
                .build();

        log.info("[AUTH-GW] auth success, userId={}", ctx.getUserId());

        return chain.filter(exchange.mutate().request(newReq).build());
    }

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        log.info("[AUTH-GW] enter JwtAuthWebFilter, path={}",
//                exchange.getRequest().getURI().getPath());
//        String path = exchange.getRequest().getURI().getPath();
//        if (isWhitelisted(path)) {
//            return chain.filter(exchange);
//        }
//        String auth = exchange.getRequest()
//                .getHeaders()
//                .getFirst(AuthConstants.AUTH_HEADER);
//        String token = AuthHeaderUtil.extractBearerToken(auth);
//        if (!StringUtils.hasText(token)) {
//            return chain.filter(exchange);
//        }
//
//        UserContext ctx = tokenService.parseToken(token);
//        ServerHttpRequest newReq = exchange.getRequest().mutate()
//                .header(AuthConstants.HEADER_USER_ID, String.valueOf(ctx.getUserId()))
//                .header(AuthConstants.HEADER_USERNAME, ctx.getUserName() == null ? "" : ctx.getUserName())
//                .build();
//
//        return chain.filter(exchange.mutate().request(newReq).build());
//    }

    private boolean isWhitelisted(String path) {
        if (properties.getWhitelist() == null) return false;
        for (String pattern : properties.getWhitelist()) {

            if (matcher.match(pattern, path)) return true;
        }
        return false;
    }
}
