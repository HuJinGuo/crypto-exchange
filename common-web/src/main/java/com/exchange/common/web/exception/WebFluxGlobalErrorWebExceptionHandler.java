package com.exchange.common.web.exception;

import com.exchange.common.core.enums.ResultCode;
import com.exchange.common.core.exception.BizException;
import com.exchange.common.core.web.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 走 ErrorWebExceptionHandler
 * 路由不存在 /xxx
 * Path Predicate 不匹配
 */
@Order(-1)
public class WebFluxGlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange,Throwable ex) {
        if(exchange.getResponse().isCommitted()){
            return Mono.error(ex);
        }

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        R<Object> body = R.fail(ResultCode.SYSTEM_ERROR.getCode(), "系统繁忙，请稍后再试");
        if (ex instanceof BizException be) {
            httpStatus = HttpStatus.OK;
            body = R.fail(be.getCode(),be.getMessage());
        } else if (ex instanceof ResponseStatusException rse) {
            if (rse.getStatusCode() == HttpStatus.NOT_FOUND) {
                httpStatus = HttpStatus.NOT_FOUND;
                body = R.fail(ResultCode.GATEWAY_NOT_FOUND);
            } else if (rse.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
                body = R.fail(ResultCode.GATEWAY_SERVICE_UNAVAILABLE);
            } else {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                body = R.fail(ResultCode.GATEWAY_ERROR);
            }    
        } // ===== Gateway：找不到路由 =====
//        else if (ex instanceof NotFoundException) {
//            httpStatus = HttpStatus.NOT_FOUND;
//            body = R.fail(ResultCode.GATEWAY_NOT_FOUND);
//        }
        // ===== 下游服务超时 =====
        else if (ex instanceof TimeoutException) {
            httpStatus = HttpStatus.GATEWAY_TIMEOUT;
            body = R.fail(ResultCode.GATEWAY_TIMEOUT);
        }

        // ===== 服务连接失败 =====
        else if (ex instanceof ConnectException) {
            httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
            body = R.fail(ResultCode.GATEWAY_SERVICE_UNAVAILABLE.getCode(),
                    "目标服务连接失败，请检查服务是否启动");
        }

        // ===== DNS 服务名找不到 =====
        else if (ex instanceof UnknownHostException) {
            httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
            body = R.fail(ResultCode.GATEWAY_SERVICE_UNAVAILABLE.getCode(),
                    "服务名解析失败，请检查 Nacos 是否已注册该服务");
        }

        // ===== 兜底：网关内部错误 =====
        else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            body = R.fail(ResultCode.GATEWAY_ERROR);
        }

        //======= JSON 输出
        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(body);

        } catch (Exception e) {
            bytes = ("{\"code\":500,\"msg\":\"系统繁忙\",\"data\":null}")
                    .getBytes(StandardCharsets.UTF_8);
        }
        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        exchange.getResponse().getHeaders().setContentLength(bytes.length);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(bytes)
        ));
    }


}
