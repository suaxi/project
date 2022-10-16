package com.software.security.controller;

import com.software.annotation.OperationLog;
import com.software.dto.ResponseResult;
import com.software.security.config.TokenProvider;
import com.software.security.dto.AuthUserDto;
import com.software.security.dto.LoginUserDto;
import com.software.security.properties.LoginProperties;
import com.software.security.properties.SecurityProperties;
import com.software.security.service.OnlineUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Hao
 * @date 2022/10/16 20:05
 */
@RestController
@RequestMapping("/auth")
@Api(tags = "认证授权接口")
public class LoginController {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private LoginProperties loginProperties;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private OnlineUserService onlineUserService;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @OperationLog("用户登录")
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ResponseResult<Map<String, Object>> login(@Validated @RequestBody AuthUserDto user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = tokenProvider.createToken(authenticate);

        //保存在线用户
        LoginUserDto loginUserDto = (LoginUserDto) authenticate.getPrincipal();
        onlineUserService.save(loginUserDto, token, request);

        Map<String, Object> map = new HashMap<>(1);
        map.put("token", token);

        if (loginProperties.isSingleLogin()) {
            //单用户登录检查
            onlineUserService.checkLoginByUsername(user.getUsername(), token);
        }
        return new ResponseResult<>(HttpStatus.OK.value(), "登录成功", map);
    }

    @ApiOperation("退出")
    @GetMapping("/logout")
    public ResponseResult<?> logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
        return new ResponseResult<>(HttpStatus.OK.value(), "退出成功");
    }
}
