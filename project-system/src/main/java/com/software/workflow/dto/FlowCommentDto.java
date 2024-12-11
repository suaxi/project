package com.software.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author suaxi
 * @date 2024/12/10 19:05
 */
@Data
@ApiModel("审批意见Dto")
public class FlowCommentDto implements Serializable {

    @ApiModelProperty(value = "意见类别：0 正常意见，1 退回意见，2 驳回意见")
    private String type;

    @ApiModelProperty("意见内容")
    private String comment;

}
