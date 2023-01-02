package com.software.oss.service;

import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.minio.messages.Upload;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2023/1/2 20:40
 */
public interface BucketService {

    /**
     * 获取配置的桶
     *
     * @return
     */
    String getConfigBucket();

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     * @throws MinioException
     */
    void addBucket(String bucketName) throws MinioException;

    /**
     * 列出所有存储桶
     *
     * @return
     * @throws MinioException
     */
    List<Bucket> listBuckets() throws MinioException;

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return
     * @throws MinioException
     */
    boolean bucketExists(String bucketName) throws MinioException;

    /**
     * 删除一个存储桶
     *
     * @param bucketName 存储桶名称
     * @throws MinioException
     */
    void removeBucket(String bucketName) throws MinioException;

    /**
     * 列出某个存储桶中的所有对象
     *
     * @param bucketName 存储桶名称
     * @param prefix     对象名称的前缀
     * @param recursive  是否递归查找，如果是false，就模拟文件夹结构查找
     * @param useVersion1 如果是true，使用版本1 REST API
     * @return
     */
    Iterable<Result<Item>> listObjects(String bucketName, String prefix, boolean recursive, boolean useVersion1);

    /**
     * 列出存储桶中被部分上传的对象
     *
     * @param bucketName 存储桶名称
     * @param prefix     对象名称的前缀，列出有该前缀的对象
     * @param recursive  是否递归查找，如果是false，就模拟文件夹结构查找
     * @return
     */
    Iterable<Result<Upload>> listIncompleteUploads(String bucketName, String prefix, boolean recursive);

}
