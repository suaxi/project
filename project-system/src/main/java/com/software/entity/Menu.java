package com.software.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:29
 */
@Data
@ApiModel("菜单表")
@TableName("sys_menu")
public class Menu implements Serializable {

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("父级菜单id")
    @TableField("pid")
    private Long pid;

    @ApiModelProperty("子菜单数量")
    @TableField("sub_count")
    private Integer subCount;

    @ApiModelProperty("菜单类型")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("组件名称")
    @TableField("component")
    private String component;

    @ApiModelProperty("图标")
    @TableField("icon")
    private String icon;

    @ApiModelProperty("路径")
    @TableField("path")
    private String path;

    @ApiModelProperty("是否外链")
    @TableField("i_frame")
    private String iFrame;

    @ApiModelProperty("缓存")
    @TableField("cache")
    private String cache;

    @ApiModelProperty("隐藏")
    @TableField("hidden")
    private String hidden;

    @ApiModelProperty("权限")
    @TableField("permission")
    private String permission;

    @ApiModelProperty("排序")
    @TableField("sort")
    private Long sort;

    @ApiModelProperty("创建人")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty("更新人")
    @TableField("update_by")
    private String updateBy;

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

}
