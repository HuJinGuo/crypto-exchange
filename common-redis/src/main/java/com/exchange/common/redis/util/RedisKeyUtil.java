package com.exchange.common.redis.util;

public class RedisKeyUtil {

    public static String build(String prefix,Object... parts){
        StringBuilder sb = new StringBuilder(prefix);
        for (Object part : parts) {
            sb.append(part).append(":");
        }
        return sb.substring(0,sb.length() - 1);
    }
}
