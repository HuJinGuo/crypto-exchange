package com.exchange.common.auth.core.utils;


import com.exchange.common.auth.core.constant.AuthConstants;
import org.springframework.util.StringUtils;

public final class AuthHeaderUtil {

    private AuthHeaderUtil() {}

    public static String extractBearerToken(String authorization) {
        if (!StringUtils.hasText(authorization)) return null;
        if (authorization.startsWith(AuthConstants.BEARER_PREFIX)) {
            return authorization.substring(AuthConstants.BEARER_PREFIX.length()).trim();
        }
        return null;
    }
}