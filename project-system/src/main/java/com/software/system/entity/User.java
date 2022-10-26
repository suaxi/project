package com.software.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:29
 */
@Data
@ApiModel("用户表")
@TableName("sys_user")
public class User implements Serializable {

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("部门id")
    @TableField("dept_id")
    private Long deptId;

    @NotBlank
    @ApiModelProperty("用户名")
    @TableField("username")
    private String username;

    @NotBlank
    @ApiModelProperty("昵称")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty("性别")
    @TableField("sex")
    private String sex;

    @NotBlank
    @ApiModelProperty("电话")
    @TableField("phone")
    private String phone;

    @Email
    @NotBlank
    @ApiModelProperty("邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("头像地址")
    @TableField("avatar_name")
    private String avatarName;

    @ApiModelProperty("头像真实路径")
    @TableField("avatar_path")
    private String avatarPath;

    @ApiModelProperty("密码")
    @TableField("password")
    private String password;

    @ApiModelProperty("是否为admin账号")
    @TableField("is_admin")
    private String isAdmin;

    @NotNull
    @ApiModelProperty(value = "状态", notes = "1启用 0禁用")
    @TableField("enabled")
    private Boolean enabled;

    @ApiModelProperty("创建人")
    @TableField("create_by")
    private Long createBy;

    @ApiModelProperty("更新人")
    @TableField("update_by")
    private Long updateBy;

    @ApiModelProperty("密码修改时间")
    @TableField("pwd_reset_time")
    private Date pwdResetTime;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "逻辑删除")
    private Integer isDelete;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色ids")
    private String roleIds;

    @TableField(exist = false)
    @ApiModelProperty(value = "岗位ids")
    private String jobIds;

}
