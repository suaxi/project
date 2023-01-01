package com.software.oss.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.dto.QueryRequest;
import com.software.enums.ContentType;
import com.software.oss.entity.OssAttachment;
import com.software.oss.service.OssAttachmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wang Hao
 * @date 2023/1/1 21:11
 */
@RestController
@RequestMapping("/oss/public")
@Api(tags = "oss附件上传-通用管理接口")
public class PublicController {

    @Autowired
    private OssAttachmentService ossAttachmentService;

    @ApiOperation("根据ID查询OSS附件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, paramType = "path", dataType = "String")
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<OssAttachment> queryById(@PathVariable("id") String id) {
        OssAttachment ossAttachment = ossAttachmentService.getById(id);
        this.setContentType(ossAttachment);
        return new ResponseEntity<>(ossAttachment, HttpStatus.OK);
    }

    @ApiOperation("分页查询部门信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ossAttachment", value = "对象存储信息", required = true, paramType = "query", dataType = "object"),
            @ApiImplicitParam(name = "queryRequest", value = "查询参数", required = true, paramType = "query", dataType = "object")
    })
    @GetMapping("/queryPage")
    public ResponseEntity<Page<OssAttachment>> queryPage(OssAttachment ossAttachment, QueryRequest queryRequest) {
        return new ResponseEntity<>(ossAttachmentService.queryPage(ossAttachment, queryRequest), HttpStatus.OK);
    }

    /**
     * 设置 ContentType
     *
     * @param ossAttachment 附件信息
     */
    private void setContentType(OssAttachment ossAttachment) {
        if (ossAttachment != null) {
            ossAttachment.setContentType(ContentType.parseOf(ossAttachment.getFileType()).getMimeType());
        }
    }
}
