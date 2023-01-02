package com.software.oss.service;

import com.software.oss.dto.OssAttachmentDto;
import com.software.oss.entity.OssAttachment;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.DeleteError;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Wang Hao
 * @date 2023/1/2 21:21
 */
public interface DocumentService {

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param filename   要上传的文件
     * @param options    上传对象选项
     * @throws MinioException
     */
    void putObject(String bucketName, String objectName, String filename, PutObjectOptions options) throws MinioException;

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param stream     要上传的流
     * @param options    上传对象选项
     * @throws MinioException
     */
    void putObject(String bucketName, String objectName, InputStream stream, PutObjectOptions options) throws MinioException;

    /**
     * 以流的形式下载一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     * @throws MinioException
     */
    byte[] getObject(String bucketName, String objectName) throws MinioException;

    /**
     * 以流的形式下载一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param sse        服务器端加密选项
     * @return
     * @throws MinioException
     */
    byte[] getObject(String bucketName, String objectName, ServerSideEncryption sse) throws MinioException;

    /**
     * 以流的形式下载一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param offset     起始字节的位置
     * @return
     * @throws MinioException
     */
    byte[] getObject(String bucketName, String objectName, long offset) throws MinioException;

    /**
     * 以流的形式下载一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度 (可选，如果无值则代表读到文件结尾)
     * @return
     * @throws MinioException
     */
    byte[] getObject(String bucketName, String objectName, long offset, long length) throws MinioException;

    /**
     * 以流的形式下载一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度 (可选，如果无值则代表读到文件结尾)
     * @param sse        服务器端加密选项
     * @return
     * @throws MinioException
     */
    byte[] getObject(String bucketName, String objectName, long offset, long length, ServerSideEncryption sse) throws MinioException;

    /**
     * 下载对象到文件
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param fileName   文件名称
     * @throws MinioException
     */
    void getObject(String bucketName, String objectName, String fileName) throws MinioException;

    /**
     * 下载对象到文件
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param sse        服务器端加密选项
     * @param fileName   文件名称
     * @throws MinioException
     */
    void getObject(String bucketName, String objectName, ServerSideEncryption sse, String fileName) throws MinioException;

    /**
     * 拷贝对象
     *
     * @param bucketName     存储桶名称
     * @param objectName     存储桶里的对象名称
     * @param headerMap      头部参数
     * @param sse            服务器端加密选项
     * @param srcBucketName  源存储桶名称
     * @param srcObjectName  源存储桶里的对象名称
     * @param srcSse         源服务器端加密选项
     * @param copyConditions 拷贝操作的一些条件（Map形式）
     * @throws MinioException
     */
    void copyObject(String bucketName, String objectName, Map<String, String> headerMap, ServerSideEncryption sse, String srcBucketName, String srcObjectName, ServerSideEncryption srcSse, CopyConditions copyConditions) throws MinioException;

    /**
     * 获取对象的元数据
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     * @throws MinioException
     */
    ObjectStat statObject(String bucketName, String objectName) throws MinioException;

    /**
     * 获取对象的元数据
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param sse        服务器端加密选项
     * @return
     * @throws MinioException
     */
    ObjectStat statObject(String bucketName, String objectName, ServerSideEncryption sse) throws MinioException;

    /**
     * 删除一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @throws MinioException
     */
    void removeObject(String bucketName, String objectName) throws MinioException;

    /**
     * 删除一个对象
     *
     * @param bucketName  存储桶名称
     * @param objectNames 存储桶里的对象名称
     * @return
     * @throws MinioException
     */
    Iterable<Result<DeleteError>> removeObjects(final String bucketName, final Iterable<String> objectNames) throws MinioException;

    /**
     * 上传文件
     *
     * @param multipartFiles 上传的文件集合
     * @param attachmentDto  对象存储文件信息
     * @return
     */
    List<OssAttachment> upload(MultipartFile[] multipartFiles, OssAttachmentDto attachmentDto);

    /**
     * 上传文件（批量）
     *
     * @param byteArray     上传的文件集合
     * @param fileNames     文件名称
     * @param attachmentDto 对象存储文件信息
     * @return
     */
    List<OssAttachment> upload(byte[][] byteArray, String[] fileNames, OssAttachmentDto attachmentDto);


    /**
     * 上传文件（单文件）
     *
     * @param fileName      文件名
     * @param fileSuffix    文件后缀
     * @param inputStream   输入流
     * @param attachmentDto 对象存储文件信息
     * @return
     */
    OssAttachment upload(String fileName, String fileSuffix, InputStream inputStream, OssAttachmentDto attachmentDto);
}
