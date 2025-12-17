package com.exchange.common.core.constant;

/**
 * 链路追踪常量定义
 * <p>
 * 用于：
 * 1. Gateway → 下游服务 Trace 透传
 * 2. MDC 日志上下文
 * 3. 后续 Kafka / ES / OpenTelemetry 扩展
 */
public interface TraceConstants {

    /* ================== MDC KEY ================== */

    /** 链路追踪 ID（全链路唯一） */
    String TRACE_ID = "traceId";

    /** 单次请求 ID（用于区分同一 trace 下的多次调用） */
    String REQUEST_ID = "requestId";


    /* ================== HTTP HEADER ================== */

    /** 请求头：TraceId */
    String TRACE_HEADER = "X-Trace-Id";

    /** 请求头：RequestId */
    String REQUEST_HEADER = "X-Request-Id";


    /* ================== Reactor Context ================== */

    /** Reactor Context 中存 traceId 的 key */
    String REACTOR_TRACE_ID = TRACE_ID;

    /** Reactor Context 中存 requestId 的 key */
    String REACTOR_REQUEST_ID = REQUEST_ID;


    /* ================== 默认值 / 兜底 ================== */

    /** 未知 TraceId */
    String UNKNOWN_TRACE_ID = "UNKNOWN";

    /** 未知 RequestId */
    String UNKNOWN_REQUEST_ID = "UNKNOWN";
}