package com.exchange.common.core.util;

import java.util.UUID;

public class IdGenerator {
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
