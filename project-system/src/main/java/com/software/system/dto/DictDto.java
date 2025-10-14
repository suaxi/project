package com.software.system.dto;

import com.software.system.entity.Dict;
import com.software.system.entity.DictDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author suaxi
 * @date 2025/10/14 21:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("数据字典Dto")
public class DictDto extends Dict implements Serializable {

    @ApiModelProperty("数据字典详情")
    private List<DictDetail> dictDetailList;
}
