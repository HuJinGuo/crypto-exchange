package com.exchange.common.core.util;

import java.util.UUID;

public class TraceIdUtil {

    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateRequestId() {
        return UUID.randomUUID().toString().substring(0, 16);
    }
}