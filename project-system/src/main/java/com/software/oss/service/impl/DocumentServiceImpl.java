package com.software.oss.service.impl;

import cn.hutool.core.util.IdUtil;
import com.software.constant.StringConstant;
import com.software.enums.ContentType;
import com.software.exception.BadRequestException;
import com.software.oss.dto.OssAttachmentDto;
import com.software.oss.entity.OssAttachment;
import com.software.oss.properties.OssProperties;
import com.software.oss.service.DocumentService;
import com.software.oss.service.OssAttachmentService;
import com.software.oss.utils.ImageUtil;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.DeleteError;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Wang Hao
 * @date 2023/1/2 21:27
 */
@Slf4j
@Service
public class DocumentServiceImpl extends BaseOssService implements DocumentService {

    /**
     * 目录分隔符
     */
    private static final String CATALOG_DELIMITER = "/";

    /**
     * 图片文件后缀
     */
    private static final String[] IMAGE_SUFFIX_LIST = new String[]{"JPEG", "JPG", "GIF", "BMP", "PNG", "WEBP", "TIF", "TIFF"};

    /**
     * PDF文件后缀
     */
    private static final String[] PDF_SUFFIX_LIST = new String[]{"PDF"};

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private OssProperties ossProperties;

    @Autowired
    private OssAttachmentService ossAttachmentService;

    @Override
    public void putObject(String bucketName, String objectName, String filename, PutObjectOptions options) throws MinioException {
        try {
            minioClient.putObject(bucketName, objectName, filename, options);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
    }

    @Override
    public void putObject(String bucketName, String objectName, InputStream stream, PutObjectOptions options) throws MinioException {
        try {
            minioClient.putObject(bucketName, objectName, stream, options);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            exceptionHandle(e);
        }
    }

    @Override
    public byte[] getObject(String bucketName, String objectName) throws MinioException {
        try (InputStream inputStream = minioClient.getObject(bucketName, objectName)) {
            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
        return null;
    }

    @Override
    public byte[] getObject(String bucketName, String objectName, ServerSideEncryption sse) throws MinioException {
        try (InputStream inputStream = minioClient.getObject(bucketName, objectName, sse)) {
            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
        return null;
    }

    @Override
    public byte[] getObject(String bucketName, String objectName, long offset) throws MinioException {
        try (InputStream inputStream = minioClient.getObject(bucketName, objectName, offset)) {
            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
        return null;
    }

    @Override
    public byte[] getObject(String bucketName, String objectName, long offset, long length) throws MinioException {
        try (InputStream inputStream = minioClient.getObject(bucketName, objectName, offset, length)) {
            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
        return null;
    }

    @Override
    public byte[] getObject(String bucketName, String objectName, long offset, long length, ServerSideEncryption sse) throws MinioException {
        try (InputStream inputStream = minioClient.getObject(bucketName, objectName, offset, length, sse)) {
            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
        return null;
    }

    @Override
    public void getObject(String bucketName, String objectName, String fileName) throws MinioException {
        try {
            minioClient.getObject(bucketName, objectName, fileName);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
    }

    @Override
    public void getObject(String bucketName, String objectName, ServerSideEncryption sse, String fileName) throws MinioException {
        try {
            minioClient.getObject(bucketName, objectName, sse, fileName);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
    }

    @Override
    public void copyObject(String bucketName, String objectName, Map<String, String> headerMap, ServerSideEncryption sse, String srcBucketName, String srcObjectName, ServerSideEncryption srcSse, CopyConditions copyConditions) throws MinioException {
        try {
            minioClient.copyObject(bucketName, objectName, headerMap, sse, srcBucketName, srcObjectName, srcSse, copyConditions);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
    }

    @Override
    public ObjectStat statObject(String bucketName, String objectName) throws MinioException {
        try {
            return minioClient.statObject(bucketName, objectName);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
        return null;
    }

    @Override
    public ObjectStat statObject(String bucketName, String objectName, ServerSideEncryption sse) throws MinioException {
        try {
            return minioClient.statObject(bucketName, objectName, sse);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
        return null;
    }

    @Override
    public void removeObject(String bucketName, String objectName) throws MinioException {
        try {
            minioClient.removeObject(bucketName, objectName);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
    }

    @Override
    public Iterable<Result<DeleteError>> removeObjects(String bucketName, Iterable<String> objectNames) throws MinioException {
        try {
            return minioClient.removeObjects(bucketName, objectNames);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
        return null;
    }

    @Override
    public List<OssAttachment> upload(MultipartFile[] multipartFiles, OssAttachmentDto attachmentDto) {
        String fileName = "";
        String fileSuffix;
        List<OssAttachment> ossAttachmentList = new ArrayList<>();
        try {
            if (multipartFiles != null && multipartFiles.length > 0) {
                for (MultipartFile multipartFile : multipartFiles) {
                    fileName = multipartFile.getOriginalFilename();
                    assert fileName != null;
                    fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
                    if (StringUtils.isBlank(fileSuffix)) {
                        fileSuffix = "UNKNOWN";
                    }
                    ossAttachmentList.add(this.upload(fileName, fileSuffix, multipartFile.getInputStream(), attachmentDto));
                }
            }
        } catch (IOException e) {
            log.error("文件[{}]上传失败：{}", fileName, e.getMessage());
            throw new BadRequestException("文件[" + fileName + "]上传失败：" + e.getMessage());
        }
        return ossAttachmentList;
    }

    @Override
    public List<OssAttachment> upload(byte[][] byteArray, String[] fileNames, OssAttachmentDto attachmentDto) {
        String fileName = "";
        String fileSuffix;
        List<OssAttachment> ossAttachmentList = new ArrayList<>();
        try {
            if (byteArray != null && byteArray.length > 0) {
                for (int i = 0; i < byteArray.length; i++) {
                    fileName = fileNames[i];
                    fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
                    if (StringUtils.isBlank(fileSuffix)) {
                        fileSuffix = "UNKNOWN";
                    }
                    ossAttachmentList.add(this.upload(fileName, fileSuffix, new ByteArrayInputStream(byteArray[i]), attachmentDto));
                }
            }
        } catch (Exception e) {
            log.error("文件[{}]上传失败：{}", fileName, e.getMessage());
            throw new RuntimeException("文件[" + fileName + "]上传失败：" + e.getMessage());
        }
        return ossAttachmentList;
    }

    @Override
    public OssAttachment upload(String fileName, String fileSuffix, InputStream inputStream, OssAttachmentDto attachmentDto) {
        long fileSize;
        StringBuffer pathBuffer;
        PutObjectOptions putObjectOptions;
        OssAttachment ossAttachment;
        ByteArrayInputStream bis = null;
        File tempFile = null;
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            bis = new ByteArrayInputStream(data);
            pathBuffer = new StringBuffer();
            // 添加业务目录
            if (StringUtils.isNotBlank(attachmentDto.getPath())) {
                // 不能已“/”开头
                if (attachmentDto.getPath().startsWith(StringConstant.SLASH)) {
                    attachmentDto.setPath(attachmentDto.getPath().substring(1));
                }

                pathBuffer.append(attachmentDto.getPath()).append(CATALOG_DELIMITER);
            }
            pathBuffer.append(IdUtil.simpleUUID()).append(".").append(fileSuffix);

            // 图片文件： 压缩 + 水印
            if (ArrayUtils.contains(IMAGE_SUFFIX_LIST, fileSuffix)) {
                tempFile = this.createTempFile(bis);
                bis.close();

                bis = new ByteArrayInputStream(Files.readAllBytes(tempFile.toPath()));
                if (attachmentDto.getWaterMark()) {
                    log.info("图片[{}]已压缩[{}%]", fileName, bis.available() / bis.available() * 100);
                    bis = ImageUtil.getInputStream(ImageUtil.setWatermark(ImageUtil.compress(ImageIO.read(bis))), fileSuffix);
                }
                putObjectOptions = new PutObjectOptions(bis.available(), -1);
            } else if (ArrayUtils.contains(PDF_SUFFIX_LIST, fileSuffix)) {
                putObjectOptions = new PutObjectOptions(bis.available(), -1);
            } else {
                // 其他文件
                putObjectOptions = new PutObjectOptions(bis.available(), -1);
            }
            putObjectOptions.setContentType(ContentType.parseOf(fileSuffix).getMimeType());

            // 上传文件
            fileSize = bis.available();
            this.putObject(ossProperties.getBucket(), pathBuffer.toString(), bis, putObjectOptions);
            log.info("文件[{}]上传成功", ossProperties.getBucket() + CATALOG_DELIMITER + pathBuffer);

            // 存储OSS附件信息
            ossAttachment = new OssAttachment();
            ossAttachment.setBucketName(ossProperties.getBucket());
            ossAttachment.setPath(attachmentDto.getPath());
            ossAttachment.setBusinessId(attachmentDto.getBusinessId());
            ossAttachment.setBusinessType(attachmentDto.getBusinessType());
            ossAttachment.setSubBusinessId(attachmentDto.getSubBusinessId());
            ossAttachment.setSubBusinessType(attachmentDto.getSubBusinessType());
            ossAttachment.setFileName(fileName);
            ossAttachment.setFileType(fileSuffix);
            ossAttachment.setFileSize(fileSize);
            ossAttachment.setFileMd5(DigestUtils.md5Hex(data));
            ossAttachment.setStoreServer(ossProperties.getEndpoint());
            ossAttachment.setStorePath(pathBuffer.toString());
            ossAttachment.setGroupId(attachmentDto.getGroupId());
            ossAttachment.setUserId(Integer.valueOf(attachmentDto.getUserId()));
            ossAttachment.setRemark(attachmentDto.getMark());
            ossAttachment.setCreateTime(new Date());
            if (ossAttachmentService.add(ossAttachment)) {
                log.info("文件[{}]信息保存成功", ossProperties.getBucket() + CATALOG_DELIMITER + pathBuffer);
            }
        } catch (IllegalArgumentException e) {
            log.error("不支持上传[{}]文件格式", fileSuffix);
            throw new BadRequestException("不支持上传[" + fileSuffix + "]文件格式");
        } catch (IOException | MinioException e) {
            log.error("文件[{}]上传失败：{}", fileName, e.getMessage());
            throw new BadRequestException("文件[" + fileName + "]上传失败：" + e.getMessage());
        } finally {
            // 关闭输入流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 删除临时文件
            if (tempFile != null) {
                tempFile.deleteOnExit();
            }
        }
        return ossAttachment;
    }

    /**
     * 创建临时文件
     *
     * @param inputStream 输入流
     * @return
     */
    private File createTempFile(InputStream inputStream) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp_file_" + System.currentTimeMillis(), ".tmp");
            OutputStream outputStream = Files.newOutputStream(tempFile.toPath());
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tempFile;
    }

}
