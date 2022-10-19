package com.software.security.controller;

import cn.hutool.core.util.IdUtil;
import com.software.annotation.OperationLog;
import com.software.constant.KeyPair;
import com.software.constant.StringConstant;
import com.software.dto.ResponseResult;
import com.software.exception.BadRequestException;
import com.software.security.config.TokenProvider;
import com.software.security.dto.AuthUserDto;
import com.software.security.dto.LoginUserDto;
import com.software.security.enums.LoginCodeEnum;
import com.software.security.properties.LoginProperties;
import com.software.security.properties.SecurityProperties;
import com.software.security.service.OnlineUserService;
import com.software.utils.RedisUtils;
import com.software.utils.RsaUtils;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedisUtils redisUtils;

    @OperationLog("用户登录")
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ResponseResult<Map<String, Object>> login(@Validated @RequestBody AuthUserDto user, HttpServletRequest request) throws Exception {
        //验证码校验
        String code = (String) redisUtils.get(user.getUuid());
        //清除对应的验证码
        redisUtils.del(user.getUuid());
        if (StringUtils.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(user.getCode()) || !user.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }

        //认证授权
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), RsaUtils.decryptByPrivateKey(KeyPair.PRIVATE_KEY, user.getPassword()));
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = tokenProvider.createToken(authenticate);

        //保存在线用户
        LoginUserDto loginUserDto = (LoginUserDto) authenticate.getPrincipal();
        onlineUserService.save(loginUserDto, token, request);

        Map<String, Object> map = new HashMap<>(1);
        map.put("token", securityProperties.getTokenStartWith() + token);

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

    @ApiOperation("获取验证码")
    @GetMapping("/getCaptcha")
    public ResponseResult<Map<String, Object>> getCaptcha() {
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = securityProperties.getCodeKey() + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String text = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.ARITHMETIC.ordinal() && text.contains(StringConstant.DOT)) {
            text = text.split("\\.")[0];
        }
        //缓存验证码
        redisUtils.set(uuid, text, loginProperties.getExpiration(), TimeUnit.MINUTES);
        Map<String, Object> map = new HashMap<>(2);
        map.put("img", captcha.toBase64());
        map.put("uuid", uuid);
        return new ResponseResult<>(HttpStatus.OK.value(), map);
    }
}
