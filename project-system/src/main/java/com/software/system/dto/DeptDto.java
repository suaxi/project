package com.software.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Wang Hao
 * @date 2022/11/2 21:37
 */
@Data
@ApiModel("部门信息dto")
public class DeptDto implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父级部门id")
    private Long pid;

    @ApiModelProperty("子部门数量")
    private Integer subCount;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty(value = "状态", notes = "1启用 0禁用")
    private Boolean enabled;

    @ApiModelProperty("排序")
    private Integer sort;

    public Boolean getHasChildren() {
        return getSubCount() > 0;
    }

    public Boolean getLeaf() {
        return getSubCount() <= 0;
    }

    public String getLabel() {
        return getName();
    }
}
