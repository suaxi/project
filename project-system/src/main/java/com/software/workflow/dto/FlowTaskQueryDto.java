package com.software.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author suaxi
 * @date 2024/12/10 19:18
 */
@Data
@ApiModel("任务查询Dto")
public class FlowTaskQueryDto implements Serializable {

    @ApiModelProperty("任务Id")
    private String taskId;

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("任务意见")
    private String comment;

    @ApiModelProperty("流程实例Id")
    private String instanceId;

    @ApiModelProperty("节点")
    private String targetKey;

    @ApiModelProperty("任务部署ID")
    private String deploymentId;

    @ApiModelProperty("流程环节定义ID")
    private String defId;

    @ApiModelProperty("子执行流ID")
    private String currentChildExecutionId;

    @ApiModelProperty("子执行流是否已执行")
    private Boolean flag;

    @ApiModelProperty("流程变量信息")
    private Map<String, Object> variables;

    @ApiModelProperty("审批人")
    private String assignee;

    @ApiModelProperty("候选人")
    private List<String> candidateUsers;

    @ApiModelProperty("审批组")
    private List<String> candidateGroups;

}
