package com.software.security.filter;

import com.software.security.config.TokenProvider;
import com.software.security.dto.OnlineUserDto;
import com.software.security.properties.SecurityProperties;
import com.software.security.service.OnlineUserService;
import com.software.security.service.UserCacheManager;
import com.software.utils.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Wang Hao
 * @date 2022/10/15 22:46
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends GenericFilterBean {

    @Autowired
    private SecurityProperties properties;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private OnlineUserService onlineUserService;

    @Autowired
    private UserCacheManager userCacheManager;

    public JwtAuthenticationTokenFilter(SecurityProperties properties, TokenProvider tokenProvider, OnlineUserService onlineUserService, UserCacheManager userCacheManager) {
        this.properties = properties;
        this.tokenProvider = tokenProvider;
        this.onlineUserService = onlineUserService;
        this.userCacheManager = userCacheManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = this.parseToken(request);
        if (StringUtils.isNotBlank(token)) {
            OnlineUserDto onlineUserDto = null;
            boolean clearUserCache = false;
            try {
                onlineUserDto = onlineUserService.get(properties.getOnlineKey() + token);
            } catch (Exception e) {
                log.error("tokenFilter获取在线用户异常：{}", ThrowableUtil.getStackTrace(e));
                clearUserCache = true;
            } finally {
                if (clearUserCache || Objects.isNull(onlineUserDto)) {
                    userCacheManager.clearUserCache(String.valueOf(tokenProvider.getClaims(token).get(TokenProvider.AUTHORITIES_KEY)));
                }
            }

            if (onlineUserDto != null && StringUtils.isNotBlank(token)) {
                Authentication authentication = tokenProvider.parseToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                //token续期
                tokenProvider.checkRenew(token);
            }
        }
        //请求头中未携带token，执行放行
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 解析token
     *
     * @param request HttpServletRequest
     * @return
     */
    private String parseToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(properties.getHeader());
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(properties.getTokenStartWith())) {
            // 去掉令牌前缀（注意Bearer与token之间的空格）
            return bearerToken.replace(properties.getTokenStartWith(), "");
        }
        return null;
    }
}
