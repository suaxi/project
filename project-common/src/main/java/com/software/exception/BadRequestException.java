package com.software.exception;

/**
 * @author Wang Hao
 * @date 2022/10/11 22:03
 */
public class BadRequestException extends RuntimeException {

    private Integer status = 400;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Integer status, String message) {
        super(message);
        this.status = status;
    }
}
