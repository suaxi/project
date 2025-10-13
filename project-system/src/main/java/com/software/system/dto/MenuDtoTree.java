package com.software.system.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.software.dto.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author suaxi
 * @date 2025/10/13 20:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("菜单信息Dto树")
public class MenuDtoTree extends Tree<MenuDto> {

    @ApiModelProperty("子菜单数量")
    private Integer subCount;

    @ApiModelProperty("菜单类型")
    private Integer type;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("组件名称")
    private String component;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("路径")
    private String path;

    @ApiModelProperty("是否外链")
    private Boolean iFrame;

    @ApiModelProperty("缓存")
    private Boolean cache;

    @ApiModelProperty("隐藏")
    private Boolean hidden;

    @ApiModelProperty("权限")
    private String permission;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
