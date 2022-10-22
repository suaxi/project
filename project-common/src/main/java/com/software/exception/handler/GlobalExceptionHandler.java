package com.software.exception.handler;

import com.software.constant.StringConstant;
import com.software.dto.ResponseResult;
import com.software.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ResponseResult<?> handlerException(Throwable e) {
        e.printStackTrace();
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseResult<?> badRequestException(BadRequestException e) {
        e.printStackTrace();
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResult<?> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        e.printStackTrace();
        return new ResponseResult<>(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
    }

    /**
     * 参数校验异常处理
     *
     * @param e MethodArgumentNotValidException
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder msg = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            msg.append(fieldError.getField()).append(fieldError.getDefaultMessage()).append(StringConstant.COMMA);
        }
        msg = new StringBuilder(msg.substring(0, msg.length() - 1));
        log.error(msg.toString());
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), msg);
    }
}
