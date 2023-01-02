package com.software.oss.service;

import io.minio.PostPolicy;
import io.minio.errors.MinioException;

import java.util.Map;

/**
 * @author Wang Hao
 * @date 2023/1/2 21:16
 * @description 签名操作
 */
public interface PresignedService {

    /**
     * 生成一个给HTTP GET请求用的presigned URL
     * 浏览器/移动端的客户端可以用这个URL进行下载（即使其所在的存储桶是私有的）
     * 失效时间，默认值为7天
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return
     * @throws MinioException
     */
    String presignedGetObject(String bucketName, String objectName, Integer expires) throws MinioException;

    /**
     * 生成一个给HTTP PUT请求用的presigned URL
     * 浏览器/移动端的客户端可以用这个URL进行上传（即使其所在的存储桶是私有的）
     * 失效时间，默认值为7天
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return
     */
    String presignedPutObject(String bucketName, String objectName, Integer expires) throws MinioException;

    /**
     * 允许给POST请求的presigned URL设置策略，比如接收对象上传的存储桶名称的策略，key名称前缀，过期策略
     *
     * @param policy 对象的post策略
     * @return
     */
    Map<String, String> presignedPostPolicy(PostPolicy policy) throws MinioException;

}
