package com.exchange.common.auth.core.service;

import com.exchange.common.auth.core.context.UserContext;

public interface TokenService {

    String createToken(UserContext userContext);

    UserContext parseToken(String token);

    void invalidate(String token);

    /**
     * 可选：踢下线（单点登录时会用）
     */
    default void invalidateByUserId(Long userId) {}
}
