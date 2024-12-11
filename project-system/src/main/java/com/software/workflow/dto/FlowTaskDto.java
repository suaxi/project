package com.software.workflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author suaxi
 * @date 2024/12/10 19:12
 */
@Data
@ApiModel("任务信息Dto")
public class FlowTaskDto implements Serializable {

    @ApiModelProperty("任务ID")
    private String taskId;

    @ApiModelProperty("任务执行ID")
    private String executionId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("任务Key")
    private String taskDefKey;

    @ApiModelProperty("任务执行人Id")
    private Integer assigneeId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("流程发起人部门名称")
    private String startDeptName;

    @ApiModelProperty("任务执行人名称")
    private String assigneeName;

    @ApiModelProperty("任务执行人部门名称")
    private String assigneeDeptName;

    @ApiModelProperty("流程发起人Id")
    private String startUserId;

    @ApiModelProperty("流程发起人名称")
    private String startUserName;

    @ApiModelProperty("流程类型")
    private String category;

    @ApiModelProperty("流程变量信息")
    private Object variables;

    @ApiModelProperty("局部变量信息")
    private Object taskLocalVars;

    @ApiModelProperty("流程部署ID")
    private String deployId;

    @ApiModelProperty("流程定义ID")
    private String procDefId;

    @ApiModelProperty("流程定义key")
    private String procDefKey;

    @ApiModelProperty("流程定义名称")
    private String procDefName;

    @ApiModelProperty("流程定义版本")
    private int procDefVersion;

    @ApiModelProperty("流程实例ID")
    private String procInsId;

    @ApiModelProperty("历史流程实例ID")
    private String hisProcInsId;

    @ApiModelProperty("任务耗时")
    private String duration;

    @ApiModelProperty("任务意见")
    private FlowCommentDto comment;

    @ApiModelProperty("候选执行人")
    private String candidate;

    @ApiModelProperty("任务创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty("任务完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date completeTime;

}
