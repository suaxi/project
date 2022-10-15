package com.software.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Wang Hao
 * @date 2022/10/15 12:51
 */
@Component
@ConfigurationProperties(prefix = "login")
public class LoginProperties {

    /**
     * redis缓存登录用户key
     */
    public static final String CACHE_KEY = "USER-LOGIN-DATA";

    /**
     * 是否限制单账号登录
     */
    private boolean singleLogin = false;

}
