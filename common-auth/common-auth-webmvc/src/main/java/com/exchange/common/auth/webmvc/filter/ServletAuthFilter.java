package com.exchange.common.auth.webmvc.filter;

import com.exchange.common.auth.core.constant.AuthConstants;
import com.exchange.common.auth.core.context.AuthProperties;
import com.exchange.common.auth.core.context.UserContext;
import com.exchange.common.auth.core.context.UserContextHolder;
import com.exchange.common.auth.core.service.TokenService;
import com.exchange.common.auth.core.utils.AuthHeaderUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServletAuthFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final AuthProperties properties;
    private final AntPathMatcher matcher = new AntPathMatcher();

    public ServletAuthFilter(TokenService tokenService, AuthProperties properties) {
        this.tokenService = tokenService;
        this.properties = properties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (properties.getWhitelist() == null) return false;
        for (String p : properties.getWhitelist()) {
            if (matcher.match(p, path)) return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 网关透传 header 优先（更快）
        String userIdHeader = request.getHeader(AuthConstants.HEADER_USER_ID);
        String usernameHeader = request.getHeader(AuthConstants.HEADER_USERNAME);

        try {
            if (StringUtils.hasText(userIdHeader)) {
                Long uid = Long.valueOf(userIdHeader);
                UserContextHolder.set(new UserContext(uid, usernameHeader));
                filterChain.doFilter(request, response);
                return;
            }

            // 没透传则走 token 解析
            String auth = request.getHeader(AuthConstants.AUTH_HEADER);
            String token = AuthHeaderUtil.extractBearerToken(auth);
            if (StringUtils.hasText(token)) {
                UserContext ctx = tokenService.parseToken(token);
                UserContextHolder.set(ctx);
            }

            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clear();
        }
    }
}