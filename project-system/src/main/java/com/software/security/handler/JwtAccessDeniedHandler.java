package com.software.security.handler;

import cn.hutool.json.JSONUtil;
import com.software.dto.ResponseResult;
import com.software.utils.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Wang Hao
 * @date 2022/10/16 21:14
 * @description 未授权异常handler
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        ResponseResult<Object> result = new ResponseResult<>(HttpStatus.FORBIDDEN.value(), "未授权");
        WebUtil.renderString(httpServletResponse, JSONUtil.toJsonStr(result));
    }
}
