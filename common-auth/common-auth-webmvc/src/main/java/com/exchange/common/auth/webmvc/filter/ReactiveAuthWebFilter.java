package com.exchange.common.auth.webmvc.filter;


import com.exchange.common.auth.core.constant.AuthConstants;
import com.exchange.common.auth.core.context.AuthProperties;
import com.exchange.common.auth.core.context.UserContext;
import com.exchange.common.auth.core.service.TokenService;
import com.exchange.common.auth.core.utils.AuthHeaderUtil;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class ReactiveAuthWebFilter implements WebFilter {

    private final TokenService tokenService;
    private final AuthProperties properties;
    private final AntPathMatcher matcher = new AntPathMatcher();

    public ReactiveAuthWebFilter(TokenService tokenService, AuthProperties properties) {
        this.tokenService = tokenService;
        this.properties = properties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        if (isWhitelisted(path)) {
            return chain.filter(exchange);
        }

        String userIdHeader = exchange.getRequest().getHeaders().getFirst(AuthConstants.HEADER_USER_ID);
        String usernameHeader = exchange.getRequest().getHeaders().getFirst(AuthConstants.HEADER_USERNAME);
        if (StringUtils.hasText(userIdHeader)) {
            UserContext ctx = new UserContext(Long.valueOf(userIdHeader), usernameHeader);
            return chain.filter(exchange).contextWrite(c -> c.put("userContext", ctx));
        }

        String auth = exchange.getRequest().getHeaders().getFirst(AuthConstants.AUTH_HEADER);
        String token = AuthHeaderUtil.extractBearerToken(auth);
        if (!StringUtils.hasText(token)) {
            return chain.filter(exchange);
        }

        UserContext ctx = tokenService.parseToken(token);
        return chain.filter(exchange).contextWrite(c -> c.put("userContext", ctx));
    }

    private boolean isWhitelisted(String path) {
        if (properties.getWhitelist() == null) return false;
        for (String pattern : properties.getWhitelist()) {
            if (matcher.match(pattern, path)) return true;
        }
        return false;
    }
}