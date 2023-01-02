package com.software.oss.service;

import io.minio.errors.MinioException;

/**
 * @author Wang Hao
 * @date 2023/1/2 21:11
 */
public interface BucketPolicyService {

    /**
     * 获得指定存储桶的存储桶策略
     *
     * @param bucketName 存储桶名称
     * @return
     * @throws MinioException
     */
    String getBucketPolicy(String bucketName) throws MinioException;

    /**
     * 给指定存储桶设置策略
     *
     * @param bucketName 存储桶名称
     * @param policy     策略
     * @throws MinioException
     */
    void setBucketPolicy(String bucketName, String policy) throws MinioException;
}
