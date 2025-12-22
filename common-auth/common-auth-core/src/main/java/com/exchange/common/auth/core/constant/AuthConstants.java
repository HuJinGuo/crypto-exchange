package com.exchange.common.auth.core.constant;

public interface AuthConstants {

    String AUTH_HEADER = "Authorization";
    String BEARER_PREFIX = "Bearer ";

    // 透传给下游的用户信息（网关写入）
    String HEADER_USER_ID = "userId";
    String HEADER_USERNAME = "username";

    // JWT claims
    String CLAIM_USER_ID = "uid";
    String CLAIM_USERNAME = "un";

    // Redis token session
    String REDIS_TOKEN_PREFIX = "auth:token:";     // auth:token:{token} -> json(UserContext)
    String REDIS_USER_PREFIX  = "auth:user:";      // 可选：auth:user:{userId} -> token

    long DEFAULT_EXPIRE_SECONDS = 7200;
}
