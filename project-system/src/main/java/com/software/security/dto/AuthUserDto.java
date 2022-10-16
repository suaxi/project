package com.software.security.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Wang Hao
 * @date 2022/10/16 21:55
 */
@Data
@ApiModel("用户登录dto")
public class AuthUserDto implements Serializable {

    @NotBlank
    @ApiModelProperty("用户名")
    private String username;

    @NotBlank
    @ApiModelProperty("用户名")
    private String password;
}
