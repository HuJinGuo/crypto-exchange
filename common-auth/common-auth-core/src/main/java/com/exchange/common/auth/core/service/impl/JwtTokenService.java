package com.exchange.common.auth.core.service.impl;

import com.exchange.common.auth.core.constant.AuthConstants;
import com.exchange.common.auth.core.context.AuthProperties;
import com.exchange.common.auth.core.context.UserContext;
import com.exchange.common.auth.core.exception.UnauthorizedException;
import com.exchange.common.auth.core.service.TokenService;
import com.exchange.common.auth.core.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

public class JwtTokenService implements TokenService {

    private final StringRedisTemplate redisTemplate;
    private final AuthProperties properties;
    private final ObjectMapper objectMapper;

    public JwtTokenService(StringRedisTemplate redisTemplate, AuthProperties properties, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public String createToken(UserContext ctx) {
        if (ctx == null || ctx.getUserId() == null) {
            throw new IllegalArgumentException("userContext/userId can not be null");
        }

        //单点登录
        if (properties.isSingleLogin()){
            invalidateByUserId(ctx.getUserId());
        }

        String token = JwtUtil.createToken(ctx, properties.getSecret(), properties.getExpireSeconds());

        String sessionKey =  AuthConstants.REDIS_TOKEN_PREFIX + token;
        String userKey = AuthConstants.REDIS_USER_PREFIX + ctx.getUserId();

        try{
            String json = objectMapper.writeValueAsString(ctx);
            redisTemplate.opsForValue().set(sessionKey, json, properties.getExpireSeconds());

            if (properties.isSingleLogin()){
                redisTemplate.opsForValue().set(userKey, token, properties.getExpireSeconds());
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return token;
    }

    @Override
    public UserContext parseToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new UnauthorizedException("The token is empty.");
        }
        // 先校验 JWT 签名/过期（防伪造）
        try{
            if (JwtUtil.isExpired(token,properties.getSecret())) {
                throw new UnauthorizedException("The token has expired.");
            }
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException("The token is illegal.",e);
        }
        //再校验 Redis session（支持登出/踢下线/续期）
        String sessionKey = AuthConstants.REDIS_TOKEN_PREFIX + token;
        String json = redisTemplate.opsForValue().get(sessionKey);
        if (!StringUtils.hasText(json)) {
            throw new UnauthorizedException("The login state does not exist or has expired");
        }
        try {
            return objectMapper.readValue(json, UserContext.class);
        } catch (Exception e) {
            throw new UnauthorizedException("登录态解析失败", e);
        }
    }

    @Override
    public void invalidate(String token) {
        if (!StringUtils.hasText(token)) return;
        redisTemplate.delete(AuthConstants.REDIS_TOKEN_PREFIX + token);
    }

    @Override
    public void invalidateByUserId(Long userId) {
        if (userId == null) return;
        String userKey = AuthConstants.REDIS_USER_PREFIX + userId;
        String oldToken = redisTemplate.opsForValue().get(userKey);
        if (StringUtils.hasText(oldToken)) {
            invalidate(oldToken);
        }
        redisTemplate.delete(userKey);
    }
}
