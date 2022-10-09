package com.software.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Wang Hao
 * @date 2022/10/9 22:30
 */
@Data
@ApiModel("查询参数")
public class QueryRequest implements Serializable {

    @ApiModelProperty(value = "当前页码")
    private int pageNum = 1;

    @ApiModelProperty(value = "当前页面数据量")
    private int pageSize = 10;

    @ApiModelProperty(value = "排序字段")
    private String field;

    @ApiModelProperty(value = "排序规则", notes = "ASC 升序，DESC 降序")
    private String order;
}
