package com.software.oss.config;

import com.software.oss.properties.OssProperties;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Wang Hao
 * @date 2023/1/2 21:03
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(OssProperties.class)
public class OssMinioConfig {

    @Autowired
    private OssProperties ossProperties;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = null;
        try {
            minioClient = new MinioClient(ossProperties.getEndpoint(), ossProperties.getAccessKey(), ossProperties.getSecretKey());
            boolean isExist = minioClient.bucketExists(ossProperties.getBucket());
            if (isExist) {
                log.info("bucket[{}] already exist", ossProperties.getBucket());
            } else {
                minioClient.makeBucket(ossProperties.getBucket());
                log.info("bucket[{}] created.", ossProperties.getBucket());
            }
        } catch (InvalidEndpointException | InvalidPortException e) {
            log.error("minio client init failed", e);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidResponseException |
                 InvalidBucketNameException | ErrorResponseException | RegionConflictException |
                 InsufficientDataException | XmlParserException | InternalException |
                 IOException e) {
            log.error("bucket create failed", e);
        }
        return minioClient;
    }
}
