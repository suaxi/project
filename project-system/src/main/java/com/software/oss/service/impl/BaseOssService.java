package com.software.oss.service.impl;

import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Wang Hao
 * @date 2023/1/2 20:43
 */
public abstract class BaseOssService {

    /**
     * Minio AIP Exception 统一处理
     *
     * @param e e
     * @throws MinioException
     */
    protected void exceptionHandle(Exception e) throws MinioException {
        if (e instanceof BucketPolicyTooLargeException) {
            throw new MinioException("存储桶策略太长");
        } else if (e instanceof ErrorResponseException) {
            throw new MinioException("执行失败");
        } else if (e instanceof InsufficientDataException) {
            throw new MinioException("在读到相应length之前就得到一个EOFException");
        } else if (e instanceof InternalException) {
            throw new MinioException("内部错误");
        } else if (e instanceof InvalidBucketNameException) {
            throw new MinioException("不合法的存储桶名称");
        } else if (e instanceof InvalidEndpointException) {
            throw new MinioException("端点无效");
        } else if (e instanceof InvalidExpiresRangeException) {
            throw new MinioException("InvalidPortException");
        } else if (e instanceof InvalidPortException) {
            throw new MinioException("端口无效");
        } else if (e instanceof InvalidResponseException) {
            throw new MinioException("响应无效");
        } else if (e instanceof RegionConflictException) {
            throw new MinioException("域冲突");
        } else if (e instanceof XmlParserException) {
            throw new MinioException("XML解析错误");
        } else if (e instanceof NoSuchAlgorithmException) {
            throw new MinioException("找不到相应的签名算法");
        } else if (e instanceof InvalidKeyException) {
            throw new MinioException("不合法的access key或者secret key");
        } else if (e instanceof IOException) {
            throw new MinioException("连接异常");
        }
        throw new MinioException(e.getMessage());
    }

}
