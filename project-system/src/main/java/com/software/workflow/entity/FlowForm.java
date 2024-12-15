package com.software.workflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author suaxi
 * @date 2024/12/10 20:35
 */
@Data
@ApiModel("流程表单表")
@TableName("flow_form")
public class FlowForm implements Serializable {

    @ApiModelProperty("表单ID")
    @TableId(value = "form_id", type = IdType.AUTO)
    private Integer formId;

    @ApiModelProperty("表单名称")
    @TableField("form_name")
    private String formName;

    @ApiModelProperty("表单内容")
    @TableField("form_content")
    private String formContent;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("创建人")
    @TableField("create_user")
    private String createUser;

    @ApiModelProperty("更新人")
    @TableField("update_user")
    private String updateUser;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
