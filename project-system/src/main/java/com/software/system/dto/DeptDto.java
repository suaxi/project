package com.software.system.dto;

import com.software.system.entity.Dept;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Wang Hao
 * @date 2022/11/2 21:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("部门信息dto")
public class DeptDto extends Dept {

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
