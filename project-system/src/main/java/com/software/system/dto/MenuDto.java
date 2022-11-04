package com.software.system.dto;

import com.software.system.entity.Menu;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Wang Hao
 * @date 2022/11/4 23:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("菜单信息dto")
public class MenuDto extends Menu {

    public Boolean getHasChildren() {
        return getSubCount() > 0;
    }

    public Boolean getLeaf() {
        return getSubCount() < 0;
    }

    public String getLabel() {
        return getTitle();
    }
}
