package com.software.oss.service.impl;

import com.software.oss.service.BucketPolicyService;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Wang Hao
 * @date 2023/1/2 21:12
 */
@Slf4j
@Service
public class BucketPolicyServiceImpl extends BaseOssService implements BucketPolicyService {

    @Autowired
    private MinioClient minioClient;

    @Override
    public String getBucketPolicy(String bucketName) throws MinioException {
        try {
            return minioClient.getBucketPolicy(bucketName);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
        return null;
    }

    @Override
    public void setBucketPolicy(String bucketName, String policy) throws MinioException {
        try {
            minioClient.setBucketPolicy(bucketName, policy);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
    }
}
