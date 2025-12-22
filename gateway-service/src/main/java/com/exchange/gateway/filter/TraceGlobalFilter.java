package com.exchange.gateway.filter;

import com.exchange.common.core.constant.TraceConstants;
import com.exchange.common.core.util.TraceIdUtil;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@Order(-100)
public class TraceGlobalFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String traceId = Optional.ofNullable(
                request.getHeaders().getFirst(TraceConstants.TRACE_HEADER)
        ).orElseGet(TraceIdUtil::generateTraceId);

        String requestId = Optional.ofNullable(
                request.getHeaders().getFirst(TraceConstants.REQUEST_HEADER)
        ).orElseGet(TraceIdUtil::generateRequestId);

        //写入request 的headers
        ServerHttpRequest mutateRequest = request.mutate()
                .header(TraceConstants.TRACE_ID, traceId)
                .header(TraceConstants.REQUEST_ID, requestId)
                .build();

        ServerWebExchange mutateExchange = exchange.mutate().request(mutateRequest).build();

        return chain.filter(mutateExchange)
                .contextWrite(ctx -> ctx
                        .put(TraceConstants.REACTOR_TRACE_ID, traceId)
                        .put(TraceConstants.REACTOR_REQUEST_ID, requestId)
                ).doOnEach(signal ->{
                    //将context 写入MDC
                    if (signal.isOnNext() || signal.isOnError()||signal.isOnComplete()){
                        // 从上下文获取指
                        signal.getContextView().getOrEmpty(TraceConstants.TRACE_ID)
                                .ifPresent(tId -> MDC.put("traceId",(String) tId));
                        signal.getContextView().getOrEmpty(TraceConstants.REQUEST_ID)
                                .ifPresent(reqId-> MDC.put("requestId",(String) reqId));
                    }
                }).doFinally(signal ->{
                    MDC.remove(TraceConstants.TRACE_ID);
                    MDC.remove(TraceConstants.REQUEST_ID);
                });
    }
}