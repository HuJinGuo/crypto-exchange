package com.exchange.common.web.exception;


import com.exchange.common.core.enums.ResultCode;
import com.exchange.common.core.exception.BizException;
import com.exchange.common.core.web.R;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MvcGlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public R<Void> handleBiz(BizException e) {
        return R.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                .orElse("参数校验失败");
        return R.fail(ResultCode.PARAM_INVALID.getCode(), message);
    }

    @ExceptionHandler(BindException.class)
    public R<Void> handleBind(BindException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                .orElse("参数绑定失败");
        return R.fail(ResultCode.PARAM_INVALID.getCode(), msg);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public R<Void> handleConstraintViolation(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream()
                .findFirst()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .orElse("参数校验失败");
        return R.fail(ResultCode.PARAM_INVALID.getCode(), msg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<Void> handleNotReadable(HttpMessageNotReadableException e) {
        return R.fail(ResultCode.PARAM_INVALID.getCode(), "请求体解析失败");
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleUnknown(Exception e) {
        // 这里先不把堆栈暴露给前端，日志你可以在业务服务自己打
        return R.fail(ResultCode.PARAM_INVALID.getCode(), "系统繁忙，请稍后再试");
    }
}
