package com.software.security.config;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.software.security.properties.SecurityProperties;
import com.software.utils.RedisUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Wang Hao
 * @date 2022/10/15 22:04
 * @description jwtUtil
 */
@Component
public class TokenProvider implements InitializingBean {

    public static final String AUTHORITIES_KEY = "user";

    @Autowired
    private SecurityProperties properties;

    @Autowired
    private RedisUtils redisUtils;

    private JwtParser jwtParser;

    private JwtBuilder jwtBuilder;

    public TokenProvider(SecurityProperties properties, RedisUtils redisUtils) {
        this.properties = properties;
        this.redisUtils = redisUtils;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] bytes = Decoders.BASE64.decode(properties.getBase64Secret());
        SecretKey key = Keys.hmacShaKeyFor(bytes);
        jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        jwtBuilder = Jwts.builder().signWith(key, SignatureAlgorithm.HS512);
    }

    /**
     * 创建Token，有效期由Redis维护
     *
     * @param authentication
     * @return
     */
    public String createToken(Authentication authentication) {
        return jwtBuilder
                .setId(IdUtil.simpleUUID())
                .claim(AUTHORITIES_KEY, authentication.getName())
                .setSubject(authentication.getName())
                .compact();
    }

    /**
     * 解析token，获取鉴权信息
     *
     * @param token
     * @return
     */
    public Authentication parseToken(String token) {
        Claims claims = this.getClaims(token);
        User user = new User(claims.getSubject(), "******", new ArrayList<>());
        return new UsernamePasswordAuthenticationToken(user, token, new ArrayList<>());
    }

    public Claims getClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    /**
     * token续期
     *
     * @param token
     */
    public void checkRenew(String token) {
        //获取token过期时间
        long time = redisUtils.getExpire(properties.getOnlineKey() + token) * 1000;
        Date expiredTime = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
        //当前时间与过期时间的时间差
        long differ = expiredTime.getTime() - System.currentTimeMillis();
        //在续期阈值内则执行续期
        if (differ < properties.getDetect()) {
            long renewTime = time + properties.getRenew();
            redisUtils.expire(properties.getOnlineKey() + token, renewTime, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 从请求中获取token
     *
     * @param request
     * @return
     */
    public String getToken(HttpServletRequest request) {
        String header = request.getHeader(properties.getHeader());
        if (header != null && header.startsWith(properties.getTokenStartWith())) {
            return header.substring(7);
        }
        return null;
    }
}
