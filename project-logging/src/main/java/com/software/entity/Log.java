package com.software.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Wang Hao
 * @date 2022/10/9 21:49
 */
@Data
@ApiModel("日志表")
@TableName("sys_log")
public class Log implements Serializable {

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("日志类型")
    @TableField("log_type")
    private String logType;

    @ApiModelProperty("描述")
    @TableField("description")
    private String description;

    @ApiModelProperty("请求方法")
    @TableField("method")
    private String method;

    @ApiModelProperty("参数")
    @TableField("params")
    private String params;

    @ApiModelProperty("请求ip")
    @TableField("request_ip")
    private String requestIp;

    @ApiModelProperty("请求耗时")
    @TableField("time")
    private Long time;

    @ApiModelProperty("用户名")
    @TableField("username")
    private String username;

    @ApiModelProperty("地址")
    @TableField("address")
    private String address;

    @ApiModelProperty("浏览器")
    @TableField("browser")
    private String browser;

    @ApiModelProperty("异常信息")
    @TableField("exception_detail")
    private String exceptionDetail;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建起始时间")
    @TableField(exist = false)
    private String createTimeFrom;

    @ApiModelProperty(value = "创建结束时间")
    @TableField(exist = false)
    private String createTimeTo;

}
