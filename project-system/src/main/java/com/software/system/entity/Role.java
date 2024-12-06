package com.software.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:29
 */
@Data
@ApiModel("角色表")
@TableName("sys_role")
public class Role {

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotBlank
    @ApiModelProperty("名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("角色级别")
    @TableField("level")
    private Integer level;

    @ApiModelProperty("描述")
    @TableField("description")
    private String description;

    @ApiModelProperty("数据权限")
    @TableField("data_scope")
    private String dataScope;

    @ApiModelProperty("创建人")
    @TableField("create_user")
    private String createUser;

    @ApiModelProperty("更新人")
    @TableField("update_user")
    private String updateUser;

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

    @ApiModelProperty("菜单列表")
    @TableField(exist = false)
    private List<Dept> depts;

    @ApiModelProperty("菜单列表")
    @TableField(exist = false)
    private List<Menu> menus;

}
