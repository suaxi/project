package com.software.security.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Wang Hao
 * @date 2022/10/15 22:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineUserDto implements Serializable {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("浏览器")
    private String browser;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("登录时间")
    private Date loginTime;
}
