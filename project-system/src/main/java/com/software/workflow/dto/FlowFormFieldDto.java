package com.software.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author suaxi
 * @date 2024/12/10 19:07
 */
@Data
@ApiModel("表单字段Dto")
public class FlowFormFieldDto implements Serializable {

    @ApiModelProperty("表单字段")
    private Object fields;

}
