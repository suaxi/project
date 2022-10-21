package com.software.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Wang Hao
 * @date 2022/10/16 21:08
 * @description 认证异常handler
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        log.error("未认证，请重新登录！");
        e.printStackTrace();
        httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "未认证，请重新登录！");
    }
}
