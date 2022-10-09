package com.software.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Wang Hao
 * @date 2022/10/9 23:22
 */
@Data
@ApiModel(value = "返回响应实体")
public class ResponseResult<T> {

    @ApiModelProperty(value = "返回状态码", notes = "200:成功")
    private Integer code;

    @ApiModelProperty(value = "描述信息")
    private String message;

    @ApiModelProperty(value = "响应数据")
    private T data;

    public ResponseResult() {
    }

    public ResponseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
