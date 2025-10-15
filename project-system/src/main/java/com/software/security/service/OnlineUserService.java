package com.software.security.service;

import com.software.security.dto.LoginUserDto;
import com.software.security.dto.OnlineUserDto;
import com.software.security.properties.SecurityProperties;
import com.software.utils.EncryptUtils;
import com.software.utils.ProjectUtils;
import com.software.utils.RedisUtils;
import com.software.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/15 22:53
 */
@Slf4j
@Service
public class OnlineUserService {

    @Autowired
    private SecurityProperties properties;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserCacheManager userCacheManager;

    public OnlineUserService(SecurityProperties properties, RedisUtils redisUtils) {
        this.properties = properties;
        this.redisUtils = redisUtils;
    }

    /**
     * 保存在线用户信息
     *
     * @param loginUserDto 登录用户
     * @param token        token
     * @param request      HttpServletRequest
     */
    public void save(LoginUserDto loginUserDto, String token, HttpServletRequest request) {
        Integer deptId = loginUserDto.getUser().getDeptId();
        String ip = ProjectUtils.getIp(request);
        String browser = ProjectUtils.getBrowser(request);
        String cityInfo = ProjectUtils.getCityInfo(ip);
        OnlineUserDto onlineUserDto = null;
        try {
            onlineUserDto = new OnlineUserDto(loginUserDto.getUsername(), loginUserDto.getUser().getNickName(), deptId, browser, ip, cityInfo, EncryptUtils.desEncrypt(token), new Date());
        } catch (Exception e) {
            log.error("生成在线用户token加密异常");
            e.printStackTrace();
        }
        redisUtils.set(properties.getOnlineKey() + token, onlineUserDto, properties.getTokenValidityInSeconds() / 1000);
    }

    /**
     * 根据过滤条件查询登录用户列表
     *
     * @param filter 过滤条件
     * @return
     */
    public List<OnlineUserDto> queryList(String filter) {
        List<String> keys = redisUtils.scan(properties.getOnlineKey() + "*");
        Collections.reverse(keys);
        List<OnlineUserDto> onlineUserDtoList = new ArrayList<>();
        for (String key : keys) {
            OnlineUserDto onlineUserDto = (OnlineUserDto) redisUtils.get(key);
            if (StringUtils.isNotBlank(filter)) {
                if (onlineUserDto.toString().contains(filter)) {
                    onlineUserDtoList.add(onlineUserDto);
                }
            } else {
                onlineUserDtoList.add(onlineUserDto);
            }
        }
        onlineUserDtoList.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUserDtoList;
    }

    /**
     * 踢出登录用户
     *
     * @param token token
     */
    public void kickOut(String token) {
        String key = properties.getOnlineKey() + token;
        redisUtils.del(key);
        userCacheManager.clearUserCache(SecurityUtils.getCurrentUsername());
    }

    /**
     * 退出登录
     *
     * @param token token
     */
    public void logout(String token) {
        String key = properties.getOnlineKey() + token;
        redisUtils.del(key);
        userCacheManager.clearUserCache(SecurityUtils.getCurrentUsername());
    }

    /**
     * 根据key查询登陆用户信息
     *
     * @param key key
     * @return
     */
    public OnlineUserDto get(String key) {
        return (OnlineUserDto) redisUtils.get(key);
    }

    /**
     * 登录时检查该用户是否已登录，已登录则踢出
     *
     * @param username     用户名
     * @param normalToken token
     */
    public void checkLoginByUsername(String username, String normalToken) {
        List<OnlineUserDto> onlineUserDtoList = this.queryList(username);
        if (onlineUserDtoList != null && onlineUserDtoList.size() > 0) {
            for (OnlineUserDto onlineUserDto : onlineUserDtoList) {
                if (onlineUserDto.getUsername().equals(username)) {
                    try {
                        String token = EncryptUtils.desDecrypt(onlineUserDto.getToken());
                        if (StringUtils.isNotBlank(normalToken) && !normalToken.equals(token)) {
                            //保留本次登录生成的token
                            this.kickOut(token);
                        } else if (StringUtils.isBlank(normalToken)) {
                            //其他情况，删除当前登录用户已存在的token
                            this.kickOut(token);
                        }
                    } catch (Exception e) {
                        log.error("登录时检查该用户是否已登录，根据用户名踢出用户加密token解析异常");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 根据用户名踢出登录用户
     *
     * @param username
     */
    @Async
    public void kicOutByUsername(String username) {
        List<OnlineUserDto> onlineUserDtoList = this.queryList(username);
        for (OnlineUserDto onlineUser : onlineUserDtoList) {
            if (onlineUser.getUsername().equals(username)) {
                String token = "";
                try {
                    token = EncryptUtils.desDecrypt(onlineUser.getToken());
                } catch (Exception e) {
                    log.error("根据用户名踢出登录用户加密token解析异常");
                    e.printStackTrace();
                }
                this.kickOut(token);
            }
        }
    }
}
