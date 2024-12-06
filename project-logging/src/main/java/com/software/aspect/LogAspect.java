package com.software.aspect;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.software.entity.Log;
import com.software.service.LogService;
import com.software.utils.ProjectUtils;
import com.software.utils.SecurityUtils;
import com.software.utils.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wang Hao
 * @date 2022/10/10 22:00
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogService logService;

    ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 切点
     */
    @Pointcut("@annotation(com.software.annotation.OperationLog)")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object sysLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        threadLocal.set(System.currentTimeMillis());
        result = joinPoint.proceed();
        long costTime = System.currentTimeMillis() - threadLocal.get();
        threadLocal.remove();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Log sysLog = new Log();
        sysLog.setLogType("INFO");
        sysLog.setTime(costTime);
        this.saveOperationLog(joinPoint, request, sysLog);
        return result;
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void sysLogAfterThrowing(JoinPoint joinPoint, Throwable e) {
        threadLocal.set(System.currentTimeMillis());
        long costTime = System.currentTimeMillis() - threadLocal.get();
        threadLocal.remove();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Log sysLog = new Log();
        sysLog.setLogType("ERROR");
        sysLog.setTime(costTime);
        sysLog.setExceptionDetail(ThrowableUtil.getStackTrace(e));
        this.saveOperationLog((ProceedingJoinPoint) joinPoint, request, sysLog);
    }

    /**
     * 保存操作日志
     *
     * @param joinPoint 切点
     * @param request   HttpServletRequest
     */
    private void saveOperationLog(ProceedingJoinPoint joinPoint, HttpServletRequest request, Log sysLog) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        //请求方法
        Method method = signature.getMethod();

        //描述
        com.software.annotation.OperationLog operationLog = method.getAnnotation(com.software.annotation.OperationLog.class);

        //方法全路径类名
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

        //ip
        String ip = ProjectUtils.getIp(request);

        sysLog.setDescription(operationLog.value());
        sysLog.setMethod(methodName);
        sysLog.setParams(this.getParams(method, joinPoint.getArgs()));
        sysLog.setRequestIp(ip);
        sysLog.setUsername(this.getCurrentUsername());
        //登录操作信息脱敏
        if ("login".equals(signature.getName()) && StringUtils.isNotBlank(sysLog.getParams())) {
            JSONObject obj = JSONUtil.parseObj(sysLog.getParams());
            sysLog.setUsername(obj.getStr("username", ""));
            sysLog.setParams(JSONUtil.toJsonStr(Dict.create().set("username", sysLog.getUsername())));
        }
        sysLog.setAddress(ProjectUtils.getCityInfo(ip));
        sysLog.setBrowser(ProjectUtils.getBrowser(request));
        logService.add(sysLog);
    }

    /**
     * 获取请求参数
     *
     * @param method 方法
     * @param args   切点参数集合
     * @return
     */
    private String getParams(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //RequestBody请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }

            //RequestBody请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<Object, Object> map = new HashMap<>(2);
                String key = parameters[i].getName();
                if (StringUtils.isNotEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.isEmpty()) {
            return "";
        }
        return argList.size() == 1 ? JSONUtil.toJsonStr(argList.get(0)) : JSONUtil.toJsonStr(argList);
    }

    /**
     * 获取当前用户名
     *
     * @return
     */
    private String getCurrentUsername() {
        try {
            return SecurityUtils.getCurrentUsername();
        } catch (Exception e) {
            return null;
        }
    }
}
