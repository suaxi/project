package com.software.oss.service.impl;

import com.software.oss.service.PresignedService;
import io.minio.MinioClient;
import io.minio.PostPolicy;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Wang Hao
 * @date 2023/1/2 21:18
 */
@Slf4j
@Service
public class PresignedServiceImpl extends BaseOssService implements PresignedService {

    @Autowired
    private MinioClient minioClient;

    @Override
    public String presignedGetObject(String bucketName, String objectName, Integer expires) throws MinioException {
        try {
            return minioClient.presignedGetObject(bucketName, objectName, expires);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
        return null;
    }

    @Override
    public String presignedPutObject(String bucketName, String objectName, Integer expires) throws MinioException {
        try {
            return minioClient.presignedPutObject(bucketName, objectName, expires);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
        return null;
    }

    @Override
    public Map<String, String> presignedPostPolicy(PostPolicy policy) throws MinioException {
        try {
            return minioClient.presignedPostPolicy(policy);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
        return null;
    }
}
