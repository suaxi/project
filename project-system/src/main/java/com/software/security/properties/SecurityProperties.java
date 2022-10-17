package com.software.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Wang Hao
 * @date 2022/10/15 22:11
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class SecurityProperties {

    /**
     * 请求头 Authorization
     */
    private String header;

    /**
     * 令牌前缀 Bearer
     */
    private String tokenStartWith;

    /**
     * 88位Base64编码
     */
    private String base64Secret;

    /**
     * 令牌过期时间（毫秒）
     */
    private Long tokenValidityInSeconds;

    /**
     * 在线用户 key
     */
    private String onlineKey;

    /**
     * 验证码 key
     */
    private String codeKey;

    /**
     * token 续期检查间隔时间
     */
    private Long detect;

    /**
     * 续期时间
     */
    private Long renew;

    public String getTokenStartWith() {
        return tokenStartWith + " ";
    }
}
