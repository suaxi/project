package com.software.security.service;

import cn.hutool.core.util.RandomUtil;
import com.software.security.dto.LoginUserDto;
import com.software.security.properties.LoginProperties;
import com.software.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author Wang Hao
 * @date 2022/10/15 12:46
 * @description 用户缓存管理
 */
@Component
public class UserCacheManager {

    @Autowired
    private RedisUtils redisUtils;

    @Value("${login.user-cache.idle-time}")
    private long idleTime;

    /**
     * 根据用户名查找缓存中的用户信息
     *
     * @param username 用户名
     * @return
     */
    public LoginUserDto getUserCache(String username) {
        if (StringUtils.isNotBlank(username)) {
            Object obj = redisUtils.hget(LoginProperties.CACHE_KEY, username);
            if (obj != null) {
                return (LoginUserDto) obj;
            }
        }
        return null;
    }

    /**
     * 添加用户缓存
     *
     * @param username    用户名
     * @param userDetails 用户信息
     */
    @Async
    public void addUserCache(String username, UserDetails userDetails) {
        if (StringUtils.isNotBlank(username)) {
            //添加随机数避免缓存集中过期
            long expiredTime = idleTime + RandomUtil.randomInt(900, 1800);
            redisUtils.hset(LoginProperties.CACHE_KEY, username, userDetails, expiredTime);
        }
    }

    /**
     * 清除用户缓存
     *
     * @param username 用户名
     */
    @Async
    public void clearUserCache(String username) {
        if (StringUtils.isNotBlank(username)) {
            redisUtils.hdel(LoginProperties.CACHE_KEY, username);
        }
    }
}
