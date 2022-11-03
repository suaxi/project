package com.software.exception.handler;

import com.software.constant.StringConstant;
import com.software.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/11 22:08
 * @description 统一异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handlerException(Throwable e) {
        e.printStackTrace();
        return this.dealExceptionInfo(ResponseErrorResult.error(e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequestException(BadRequestException e) {
        e.printStackTrace();
        return this.dealExceptionInfo(ResponseErrorResult.error(e.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        e.printStackTrace();
        return this.dealExceptionInfo(ResponseErrorResult.error(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage()));
    }

    /**
     * 参数校验异常处理
     *
     * @param e MethodArgumentNotValidException
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder msg = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            msg.append(fieldError.getField()).append(fieldError.getDefaultMessage()).append(StringConstant.COMMA);
        }
        msg = new StringBuilder(msg.substring(0, msg.length() - 1));
        log.error(msg.toString());
        return this.dealExceptionInfo(ResponseErrorResult.error(msg.toString()));
    }

    /**
     * 统一处理异常返回信息
     *
     * @param result 异常信息返回结果
     * @return ResponseEntity
     */
    private ResponseEntity<ResponseErrorResult> dealExceptionInfo(ResponseErrorResult result) {
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getCode()));
    }
}
