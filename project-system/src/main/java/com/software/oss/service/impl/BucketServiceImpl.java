package com.software.oss.service.impl;

import com.software.oss.properties.OssProperties;
import com.software.oss.service.BucketService;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.minio.messages.Upload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2023/1/2 20:43
 */
@Slf4j
@Service
public class BucketServiceImpl extends BaseOssService implements BucketService {

    @Resource
    private MinioClient minioClient;

    @Autowired
    private OssProperties ossProperties;

    @Override
    public String getConfigBucket() {
        return ossProperties.getBucket();
    }

    @Override
    public void addBucket(String bucketName) throws MinioException {
        try {
            minioClient.makeBucket(bucketName);
        } catch (InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
    }

    @Override
    public List<Bucket> listBuckets() throws MinioException {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
        return null;
    }

    @Override
    public boolean bucketExists(String bucketName) throws MinioException {
        try {
            return minioClient.bucketExists(bucketName);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
        return false;
    }

    @Override
    public void removeBucket(String bucketName) throws MinioException {
        try {
            minioClient.removeBucket(bucketName);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandle(e);
        }
    }

    @Override
    public Iterable<Result<Item>> listObjects(String bucketName, String prefix, boolean recursive, boolean useVersion1) {
        return minioClient.listObjects(bucketName, prefix, recursive, useVersion1);
    }

    @Override
    public Iterable<Result<Upload>> listIncompleteUploads(String bucketName, String prefix, boolean recursive) {
        return minioClient.listIncompleteUploads(bucketName, prefix, recursive);
    }
}
