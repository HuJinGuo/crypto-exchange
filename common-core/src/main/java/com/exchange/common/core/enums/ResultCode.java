
package com.exchange.common.core.enums;

import lombok.Getter;

@Getter
public enum ResultCode {

  SUCCESS(200, "success"),
  PARAM_INVALID(400, "参数错误"),
  UNAUTHORIZED(401, "未登录"),
  FORIBIDDEN(403, "无权限"),
  NOT_FOUND(404, "资源不存在"),
  SYSTEM_ERROR(500, "系统异常"),


  GATEWAY_NOT_FOUND(40401, "网关未找到资源"),
  GATEWAY_SERVICE_UNAVAILABLE(50301, "服务不可用"),
  GATEWAY_TIMEOUT(50401, "网关超时"),
  GATEWAY_ERROR(50001, "网关内部错误");


  private final int code;
  private final String msg;

  ResultCode(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }
}
