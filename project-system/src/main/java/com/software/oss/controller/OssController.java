package com.software.oss.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.dto.QueryRequest;
import com.software.enums.ContentType;
import com.software.exception.BadRequestException;
import com.software.oss.dto.OssAttachmentDto;
import com.software.oss.entity.OssAttachment;
import com.software.oss.service.BucketService;
import com.software.oss.service.DocumentService;
import com.software.oss.service.OssAttachmentService;
import io.minio.errors.MinioException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Wang Hao
 * @date 2023/1/2 22:09
 */
@Slf4j
@RestController
@RequestMapping("/oss")
@Api(tags = "oss对象存储接口")
public class OssController {

    @Autowired
    private BucketService bucketService;

    @Autowired
    private DocumentService documentService;

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

    @ApiOperation("分页查OSS附件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ossAttachment", value = "对象存储信息", required = true, paramType = "query", dataType = "object"),
            @ApiImplicitParam(name = "queryRequest", value = "查询参数", required = true, paramType = "query", dataType = "object")
    })
    @GetMapping("/page")
    public ResponseEntity<Page<OssAttachment>> page(OssAttachment ossAttachment, QueryRequest queryRequest) {
        return new ResponseEntity<>(ossAttachmentService.queryPage(ossAttachment, queryRequest), HttpStatus.OK);
    }

    /**
     * 上传文件
     * 不填写@ApiImplicitParams注释（方便swagger测试）
     *
     * @param multipartFiles 文件
     * @param attachmentDto  文件信息
     * @return
     */
    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("files") MultipartFile[] multipartFiles, @Validated OssAttachmentDto attachmentDto) {
        return new ResponseEntity<>(documentService.upload(multipartFiles, attachmentDto), HttpStatus.OK);
    }

    @ApiOperation("根据ID下载文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, paramType = "path", dataType = "String")
    })
    @GetMapping("/download/{id}")
    public ResponseEntity<?> download(@PathVariable("id") String id) {
        OssAttachment ossAttachment = ossAttachmentService.getById(id);
        if (ossAttachment == null) {
            return null;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            byte[] byteArray = documentService.getObject(ossAttachment.getBucketName(), ossAttachment.getStorePath());
            String fileName = URLEncoder.encode(ossAttachment.getFileName(), StandardCharsets.UTF_8.toString());
            headers.add("Access-Control-Expose-Headers", "fileName=\"" + fileName + "\"");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Content-Type", ContentType.parseOf(ossAttachment.getFileType()).getMimeType());
            return new ResponseEntity<>(byteArray, headers, HttpStatus.OK);
        } catch (MinioException | IOException e) {
            log.error("文件[{}]下载失败:{}", id, e.getMessage());
            throw new BadRequestException("文件下载失败：" + e.getMessage());
        }
    }

    @ApiOperation("根据ID获取图片base64")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, paramType = "path", dataType = "String")
    })
    @GetMapping("/image-to-base64/{id}")
    public ResponseEntity<?> imageToBase64(@PathVariable("id") String id) {
        OssAttachment ossAttachment = ossAttachmentService.getById(id);
        if (ossAttachment == null) {
            return null;
        }

        try {
            byte[] byteArray = documentService.getObject(ossAttachment.getBucketName(), ossAttachment.getStorePath());
            return new ResponseEntity<>(DatatypeConverter.printBase64Binary(byteArray), HttpStatus.OK);
        } catch (MinioException e) {
            log.error("获取[{}]图片base64异常:{}", id, e.getMessage());
            throw new BadRequestException("获取图片base64异常：" + e.getMessage());
        }
    }

    @ApiOperation("根据ID删除附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, paramType = "path", dataType = "String")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        OssAttachment ossAttachment = ossAttachmentService.getById(id);
        if (ossAttachment == null) {
            throw new BadRequestException("文件不存在");
        }
        try {
            documentService.removeObject(bucketService.getConfigBucket(), ossAttachment.getStorePath());
            log.info("文件[{}]已从OSS对象存储中删除", id);
            if (ossAttachmentService.removeById(id)) {
                log.info("文件[{}]已从OSS附件信息记录中删除", id);
            }
        } catch (MinioException e) {
            log.error("文件[{}]删除失败:{}", id, e.getMessage());
            throw new BadRequestException("文件删除失败：" + e.getMessage());
        }
        return new ResponseEntity<>("文件已删除", HttpStatus.OK);
    }

    /**
     * 设置 ContentType（单文件）
     *
     * @param ossAttachment
     */
    private void setContentType(OssAttachment ossAttachment) {
        ossAttachment.setContentType(ContentType.parseOf(ossAttachment.getFileType()).getMimeType());
    }

}
