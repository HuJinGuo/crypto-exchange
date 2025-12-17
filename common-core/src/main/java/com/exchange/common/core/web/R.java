package com.exchange.common.core.web;

import com.exchange.common.core.enums.ResultCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {
    private int code;
    private String message;
    private T data;

    public static <T> R<T> ok(){
      return new R<>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMsg(),null);

    }

    public static <T> R<T> ok(T data){
        return new R<>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMsg(),data);
    }

    public static <T> R<T> fail(String message){
        return new R<>(ResultCode.SYSTEM_ERROR.getCode(), message, null);
    }

    public static <T> R<T> fail(ResultCode code){
        return new R<>(code.getCode(),code.getMsg(),null);
    }

    public static <T> R<T> fail(int code, String message) {
        return new R<>(code,message,null);
    }
}
