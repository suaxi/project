package com.software.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Wang Hao
 * @date 2022/11/3 21:35
 * @description 异常信息返回结果
 */
@Data
public class ResponseErrorResult {

    private Integer code = 400;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private String message;

    public ResponseErrorResult() {
        this.timestamp = LocalDateTime.now();
    }

    public static ResponseErrorResult error(String message) {
        ResponseErrorResult result = new ResponseErrorResult();
        result.setMessage(message);
        return result;
    }

    public static ResponseErrorResult error(Integer code, String message) {
        ResponseErrorResult result = new ResponseErrorResult();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
