package com.software.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author suaxi
 * @date 2024/12/10 19:15
 */
@Data
@ApiModel("viewer Dto")
public class FlowViewerDto implements Serializable {

    @ApiModelProperty("流程key")
    private String key;

    @ApiModelProperty("是否完成审批")
    private boolean completed;

}
