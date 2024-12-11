package com.software.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author suaxi
 * @date 2024/12/10 19:10
 */
@Data
@ApiModel("任务节点信息Dto")
public class FlowNextNodeDto implements Serializable {

    @ApiModelProperty("审批人类型")
    private String type;

    @ApiModelProperty("是否需要动态指定任务审批人")
    private String dataType;

    @ApiModelProperty("流程变量")
    private String vars;

}
