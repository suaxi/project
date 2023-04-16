package com.software.aspect;

import com.google.common.collect.ImmutableList;
import com.software.annotation.Limit;
import com.software.constant.StringConstant;
import com.software.enums.LimitType;
import com.software.exception.BadRequestException;
import com.software.utils.ProjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author Wang Hao
 * @date 2023/4/16 16:13
 */
@Slf4j
@Aspect
@Component
public class LimitAspect {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Pointcut("@annotation(com.software.annotation.Limit)")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Limit limit = method.getAnnotation(Limit.class);
        LimitType limitType = limit.limitType();
        String key = limit.key();
        if (StringUtils.isEmpty(key)) {
            if (limitType == LimitType.IP) {
                key = ProjectUtils.getIp(request);
            } else {
                key = signature.getName();
            }
        }
        ImmutableList<Object> keys = ImmutableList.of(StringUtils.join(limit.prefix(), StringConstant.UNDER_LINE, key, StringConstant.UNDER_LINE, request.getRequestURI().replace(StringConstant.DIVIDE, StringConstant.UNDER_LINE)));
        String luaScript = this.buildLuaScript();
        RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
        Number count = redisTemplate.execute(redisScript, keys, limit.count(), limit.period());
        if (null != count && count.intValue() <= limit.count()) {
            log.info("第{}次访问key为 {}，描述为 [{}]的接口", count, keys, limit.name());
            return joinPoint.proceed();
        } else {
            throw new BadRequestException("访问太频繁，请稍后再试！");
        }
    }

    private String buildLuaScript() {
        return "local c" +
                "\nc = redis.call('get', KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr', KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire', KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }
}
