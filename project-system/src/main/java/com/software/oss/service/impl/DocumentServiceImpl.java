package com.software.oss.service.impl;

import cn.hutool.core.util.IdUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfImageObject;
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
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
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
     * 图像的乘法因子
     */
    public static final float FACTOR = 0.5f;

    /**
     * 图片文件后缀
     */
    private static final String[] IMAGE_SUFFIX_LIST = new String[]{"JPEG", "JPG", "GIF", "BMP", "PNG"};

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
        String fileSuffix = "";
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
        String fileSuffix = "";
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
        long fileSize = 0L;
        StringBuffer pathBuffer = null;
        PutObjectOptions putObjectOptions = null;
        OssAttachment ossAttachment = null;
        File tempFile = null;
        try {
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
                tempFile = this.createTempFile(inputStream);
                inputStream.close();

                inputStream = Files.newInputStream(tempFile.toPath());
                // 重新打开流
                if (attachmentDto.isWaterMark()) {
                    Map<String, String> metadata = ImageUtil.readPicInfo(tempFile);
                    inputStream = ImageUtil.getInputStream(ImageUtil.setWatermark(ImageUtil.compress(ImageIO.read(inputStream)), metadata), fileSuffix);
                }
                putObjectOptions = new PutObjectOptions(inputStream.available(), -1);
                log.info("图片[{}]已压缩[{}%]", fileName, inputStream.available() / inputStream.available() * 100);
            }
            // PDF文件： 压缩
            else if (ArrayUtils.contains(PDF_SUFFIX_LIST, fileSuffix)) {
                tempFile = this.compressPdf(fileName, inputStream);
                inputStream = Files.newInputStream(tempFile.toPath());
                putObjectOptions = new PutObjectOptions(inputStream.available(), -1);
                log.info("PDF[{}]已压缩[{}%]", fileName, inputStream.available() / inputStream.available() * 100);
            }
            // 其他文件
            else {
                putObjectOptions = new PutObjectOptions(inputStream.available(), -1);
            }
            putObjectOptions.setContentType(ContentType.parseOf(fileSuffix).getMimeType());

            // 上传文件
            fileSize = inputStream.available();
            this.putObject(ossProperties.getBucket(), pathBuffer.toString(), inputStream, putObjectOptions);
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
            ossAttachment.setFileMd5(DigestUtils.md5Hex(inputStream));
            ossAttachment.setStoreServer(ossProperties.getEndpoint());
            ossAttachment.setStorePath(pathBuffer.toString());
            ossAttachment.setGroupId(attachmentDto.getGroupId());
            ossAttachment.setUserId(Long.valueOf(attachmentDto.getUserId()));
            ossAttachment.setMark(attachmentDto.getMark());
            ossAttachment.setCreateTime(new Date());
            if (ossAttachmentService.add(ossAttachment)) {
                log.info("文件[{}]信息保存成功", ossProperties.getBucket() + CATALOG_DELIMITER + pathBuffer);
            }
        } catch (DocumentException e) {
            log.error("PDF文件[{}]压缩异常：{}", fileName, e.getMessage());
            throw new BadRequestException("PDF文件[" + fileName + "]压缩异常：" + e.getMessage());
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

    /**
     * 压缩 PDF 文件
     *
     * @param fileName    文件名称
     * @param inputStream 输入流
     * @throws IOException
     * @throws DocumentException
     */
    public File compressPdf(String fileName, InputStream inputStream) throws IOException, DocumentException {
        File tempFile = File.createTempFile(fileName, ".tmp");
        // 读取pdf文件
        PdfReader pdfReader = new PdfReader(inputStream);
        int xrefSize = pdfReader.getXrefSize();
        PdfObject pdfObject = null;
        PRStream prStream = null;
        // 查找图像并处理图像流
        for (int i = 0; i < xrefSize; i++) {
            pdfObject = pdfReader.getPdfObject(i);
            if (pdfObject == null || !pdfObject.isStream()) {
                continue;
            }
            prStream = (PRStream) pdfObject;
            PdfObject pdfsubtype = prStream.get(PdfName.SUBTYPE);
            if (pdfsubtype != null && pdfsubtype.toString().equals(PdfName.IMAGE.toString())) {
                PdfImageObject image = new PdfImageObject(prStream);
                BufferedImage bi = image.getBufferedImage();
                if (bi == null) {
                    continue;
                }
                int width = (int) (bi.getWidth() * FACTOR);
                int height = (int) (bi.getHeight() * FACTOR);
                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                AffineTransform affineTransform = AffineTransform.getScaleInstance(FACTOR, FACTOR);
                Graphics2D graphics2D = bufferedImage.createGraphics();
                graphics2D.drawRenderedImage(bi, affineTransform);
                ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
                // 判断文件流的大小，超过500k的才进行压缩，否则不进行压缩
                if (bufferedImage.getData().getDataBuffer().getSize() > 500 * 1024) {
                    ImageIO.write(bufferedImage, "JPG", imgBytes);
                    prStream.clear();
                    prStream.setData(imgBytes.toByteArray(), false, PRStream.BEST_COMPRESSION);
                    prStream.put(PdfName.TYPE, PdfName.XOBJECT);
                    prStream.put(PdfName.SUBTYPE, PdfName.IMAGE);
                    prStream.put(PdfName.FILTER, PdfName.DCTDECODE);
                    prStream.put(PdfName.WIDTH, new PdfNumber(width));
                    prStream.put(PdfName.HEIGHT, new PdfNumber(height));
                    prStream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
                    prStream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
                } else {
                    ImageIO.write(bufferedImage, "JPG", imgBytes);
                }
                prStream.clear();
                prStream.setData(imgBytes.toByteArray(), false, PRStream.BEST_COMPRESSION);
                prStream.put(PdfName.TYPE, PdfName.XOBJECT);
                prStream.put(PdfName.SUBTYPE, PdfName.IMAGE);
                prStream.put(PdfName.FILTER, PdfName.DCTDECODE);
                prStream.put(PdfName.WIDTH, new PdfNumber(width));
                prStream.put(PdfName.HEIGHT, new PdfNumber(height));
                prStream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
                prStream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
            }
        }
        // Save altered PDF
        PdfStamper pdfStamper = new PdfStamper(pdfReader, Files.newOutputStream(tempFile.toPath()));
        pdfStamper.close();
        pdfReader.close();
        return tempFile;
    }
}
