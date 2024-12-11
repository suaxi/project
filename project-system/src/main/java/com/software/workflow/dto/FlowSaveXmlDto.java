package com.software.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author suaxi
 * @date 2024/12/10 19:11
 */
@Data
@ApiModel("xml Dto")
public class FlowSaveXmlDto implements Serializable {

    @ApiModelProperty("流程名称")
    private String name;

    @ApiModelProperty("流程分类")
    private String category;

    @ApiModelProperty("xml 文件")
    private String xml;

}
