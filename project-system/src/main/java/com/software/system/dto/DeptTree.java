package com.software.system.dto;

import com.software.dto.Tree;
import com.software.system.entity.Dept;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Wang Hao
 * @date 2022/11/1 22:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("部门树")
public class DeptTree extends Tree<Dept> {

    @ApiModelProperty("子部门数量")
    private Integer subCount;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("排序")
    private Long sort;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("更新人")
    private String updateBy;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
