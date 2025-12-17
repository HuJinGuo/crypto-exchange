package com.exchange.gateway.filter;

import com.exchange.common.core.enums.ResultCode;
import com.exchange.common.core.web.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 请求 /user/ping
 * → Gateway RoutingFilter
 * → Reactor Netty HttpClient 连接下游失败
 * → Gateway 内部直接 setStatus(503)
 * → Response 已经 committed
 * → ❌ ErrorWebExceptionHandler 根本进不来
 * 走ErrorWebExceptionHandler
 * 下游服务没启动
 * lb 连接失败
 * Netty 连接异常
 */
@Component
public class GatewayErrorGlobalFilter implements GlobalFilter, Ordered {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        return chain.filter(exchange)
                .onErrorResume(ex -> {

                    if (exchange.getResponse().isCommitted()) {
                        return Mono.error(ex);
                    }

                    R<Object> body = R.fail(
                            ResultCode.GATEWAY_SERVICE_UNAVAILABLE.getCode(),
                            "服务暂不可用，请稍后再试"
                    );

                    byte[] bytes;
                    try {
                        bytes = objectMapper.writeValueAsBytes(body);
                    } catch (Exception e) {
                        bytes = "{\"code\":50301,\"msg\":\"服务不可用\",\"data\":null}"
                                .getBytes(StandardCharsets.UTF_8);
                    }

                    exchange.getResponse().setStatusCode(HttpStatus.OK);
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    exchange.getResponse().getHeaders().setContentLength(bytes.length);

                    return exchange.getResponse().writeWith(
                            Mono.just(exchange.getResponse()
                                    .bufferFactory().wrap(bytes))
                    );
                });
    }

    @Override
    public int getOrder() {
        return -1; // 必须最高优先级
    }
}