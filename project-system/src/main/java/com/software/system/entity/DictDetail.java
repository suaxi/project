package com.software.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Wang Hao
 * @date 2022/12/08 21:41
 */
@Data
@ApiModel("数据字典详情表")
@TableName("sys_dict_detail")
public class DictDetail {

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("字典id")
    @TableField("dict_id")
    private Long dictId;

    @ApiModelProperty("字典标签")
    @TableField("label")
    private String label;

    @ApiModelProperty("字典值")
    @TableField("value")
    private String value;

    @ApiModelProperty("排序")
    @TableField("sort")
    private Long sort;

    @ApiModelProperty("创建人")
    @TableField("create_by")
    private Long createBy;

    @ApiModelProperty("更新人")
    @TableField("update_by")
    private Long updateBy;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
