package com.software.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author suaxi
 * @date 2024/12/10 19:33
 */
@Data
@ApiModel("退回任务节点Dto")
public class ReturnTaskNodeDto implements Serializable {

    @ApiModelProperty("任务Id")
    private String id;

    @ApiModelProperty("用户Id")
    private String name;

}
