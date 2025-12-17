package com.exchange.common.core.exception;

import com.exchange.common.core.enums.ResultCode;
import lombok.Getter;

@Getter
public class BizException extends RuntimeException {


    private final int code;

    public BizException(String message) {
        super(message);
        this.code = ResultCode.SYSTEM_ERROR.getCode();
    }

    public BizException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }
}
