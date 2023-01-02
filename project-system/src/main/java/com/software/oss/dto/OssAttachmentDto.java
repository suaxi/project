package com.software.oss.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Wang Hao
 * @date 2023/1/2 21:22
 */
@Data
public class OssAttachmentDto implements Serializable {

    @ApiModelProperty(value = "待存储路径")
    private String path;

    @ApiModelProperty(value = "业务ID")
    private String businessId;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "子业务ID")
    private String subBusinessId;

    @ApiModelProperty(value = "子业务类型")
    private String subBusinessType;

    @ApiModelProperty(value = "属组ID")
    private String groupId;

    @ApiModelProperty(value = "创建人ID")
    private String userId;

    @ApiModelProperty(value = "备注")
    private String mark;

    @ApiModelProperty(value = "是否添加水印")
    private boolean waterMark;
}
