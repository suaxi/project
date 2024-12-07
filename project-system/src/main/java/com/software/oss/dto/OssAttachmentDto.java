package com.software.oss.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author Wang Hao
 * @date 2023/1/2 21:22
 */
@Data
public class OssAttachmentDto implements Serializable {

    @NotEmpty(message = "待存储路径不能为空")
    @ApiModelProperty(value = "待存储路径")
    private String path;

    @NotEmpty(message = "业务ID不能为空")
    @ApiModelProperty(value = "业务ID")
    private String businessId;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "子业务ID")
    private String subBusinessId;

    @ApiModelProperty(value = "子业务类型")
    private String subBusinessType;

    @NotEmpty(message = "属组ID不能为空")
    @ApiModelProperty(value = "属组ID")
    private String groupId;

    @NotEmpty(message = "创建人ID不能为空")
    @ApiModelProperty(value = "创建人ID")
    private String userId;

    @ApiModelProperty(value = "备注")
    private String mark;

    @ApiModelProperty(value = "是否添加水印")
    private Boolean waterMark = false;
}
