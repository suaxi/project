package com.software.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author suaxi
 * @date 2024/12/10 20:42
 */
@Data
@ApiModel("流程部署实例表单关联表")
@TableName("flow_deploy_ins_form")
public class FlowDeployInsForm implements Serializable {

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotNull(message = "表单ID不能为空")
    @ApiModelProperty("表单ID")
    @TableField("form_id")
    private Integer formId;

    @NotNull(message = "流程部署ID不能为空")
    @ApiModelProperty("流程部署ID")
    @TableField("deploy_id")
    private String deployId;

}
