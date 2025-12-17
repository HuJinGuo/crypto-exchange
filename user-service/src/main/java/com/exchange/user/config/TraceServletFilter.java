package com.exchange.user.config;

import com.exchange.common.core.constant.TraceConstants;
import com.exchange.common.core.util.TraceIdUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;


@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@Slf4j
public class TraceServletFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String traceId = Optional.ofNullable(request.getHeader(TraceConstants.TRACE_ID))
                .orElseGet(TraceIdUtil::generateTraceId);
        String requestId = Optional.ofNullable(request.getHeader(TraceConstants.REQUEST_ID))
                .orElseGet(TraceIdUtil::generateRequestId);

        // ✅ 1️⃣ 先放 MDC
        MDC.put(TraceConstants.TRACE_ID, traceId);
        MDC.put(TraceConstants.REQUEST_ID, requestId);

        // ✅ 2️⃣ 再打印日志（这一步你之前顺序错了）
        log.info("incoming request {} {}", request.getMethod(), request.getRequestURI());

        // 继续传递
        response.setHeader(TraceConstants.TRACE_ID, traceId);
        response.setHeader(TraceConstants.REQUEST_ID, requestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TraceConstants.TRACE_ID);
            MDC.remove(TraceConstants.REQUEST_ID);
        }
    }
}
