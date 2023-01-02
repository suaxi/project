package com.software.oss.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Wang Hao
 * @date 2023/1/2 20:45
 */
@Data
@ConfigurationProperties(prefix = "oss.minio")
public class OssProperties {

    /**
     * 对象存储服务的URL
     */
    private String endpoint = "";

    /**
     * 存储桶名称
     */
    private String bucket = "";

    /**
     * Access key
     */
    private String accessKey = "";

    /**
     * Secret key
     */
    private String secretKey = "";
}
