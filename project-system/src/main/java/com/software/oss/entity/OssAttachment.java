package com.software.oss.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Wang Hao
 * @date 2023/1/1 20:56
 */
@Data
@ApiModel(description = "对象存储信息")
@TableName("sys_oss_attachment")
public class OssAttachment implements Serializable {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "存储桶名称")
    @TableField("bucket_name")
    private String bucketName;

    @ApiModelProperty(value = "待存储路径")
    @TableField("path")
    private String path;

    @ApiModelProperty(value = "业务ID")
    @TableField("business_id")
    private String businessId;

    @ApiModelProperty(value = "业务类型")
    @TableField("business_type")
    private String businessType;

    @ApiModelProperty(value = "子业务ID")
    @TableField("sub_business_id")
    private String subBusinessId;

    @ApiModelProperty(value = "子业务类型")
    @TableField("sub_business_type")
    private String subBusinessType;

    @ApiModelProperty(value = "文件名称")
    @TableField("file_name")
    private String fileName;

    @ApiModelProperty(value = "文件类型")
    @TableField("file_type")
    private String fileType;

    @ApiModelProperty(value = "文件大小")
    @TableField("file_size")
    private Long fileSize;

    @ApiModelProperty(value = "文件MD5")
    @TableField("file_md5")
    private String fileMd5;

    @ApiModelProperty(value = "存储服务器信息")
    @TableField("store_server")
    private String storeServer;

    @ApiModelProperty(value = "存储路径信息")
    @TableField("store_path")
    private String storePath;

    @ApiModelProperty(value = "属组ID")
    @TableField("group_id")
    private String groupId;

    @ApiModelProperty(value = "属主ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "备注")
    @TableField("mark")
    private String mark;

    @ApiModelProperty(value = "Content-Type")
    private transient String contentType;

}
