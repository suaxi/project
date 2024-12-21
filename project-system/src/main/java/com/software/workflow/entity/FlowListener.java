package com.software.workflow.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author suaxi
 * @date 2024/12/21 17:45
 */
@Data
@ApiModel("流程监听表")
@TableName("flow_listener")
public class FlowListener implements Serializable {

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotEmpty(message = "监听名称不能为空")
    @ApiModelProperty("监听名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("类型")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("事件类型")
    @TableField("event_type")
    private String eventType;

    @ApiModelProperty("值类型")
    @TableField("value_type")
    private String valueType;

    @ApiModelProperty("执行内容")
    @TableField("execute_content")
    private String executeContent;

    @ApiModelProperty("状态（0：禁用，1：启用）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("创建人")
    @TableField("create_user")
    private String createUser;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty("更新人")
    @TableField("update_user")
    private String updateUser;

    @TableLogic
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "逻辑删除")
    private Integer isDelete;

}
