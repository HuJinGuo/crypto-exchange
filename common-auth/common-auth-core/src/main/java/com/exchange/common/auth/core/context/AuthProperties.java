package com.exchange.common.auth.core.context;


import com.exchange.common.auth.core.constant.AuthConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "exchange.auth")
public class AuthProperties {

    /**
     * JWT secret（至少 32 字符，HS256）
     */
    private String secret = "change_me_to_a_long_long_secret_32_chars_min";

    /**
     * token 过期秒数
     */
    private long expireSeconds = AuthConstants.DEFAULT_EXPIRE_SECONDS;

    /**
     * 网关/服务的白名单（不鉴权）
     * 例：/user/login, /ping, /actuator/**
     */
    private List<String> whitelist = new ArrayList<>();

    /**
     * 是否启用“单点登录”（同一 userId 只允许一个 token）
     */
    private boolean singleLogin = false;

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public long getExpireSeconds() { return expireSeconds; }
    public void setExpireSeconds(long expireSeconds) { this.expireSeconds = expireSeconds; }

    public List<String> getWhitelist() { return whitelist; }
    public void setWhitelist(List<String> whitelist) { this.whitelist = whitelist; }

    public boolean isSingleLogin() { return singleLogin; }
    public void setSingleLogin(boolean singleLogin) { this.singleLogin = singleLogin; }
}
