package com.software.annotation;

import com.software.enums.LimitType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Wang Hao
 * @date 2023/4/16 16:10
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

    //接口名称
    String name() default "";

    //key
    String key() default "";

    //key prefix
    String prefix() default "limit";

    //事件，单位秒
    int period();

    //限制访问次数
    int count();

    //限制类型
    LimitType limitType() default LimitType.CUSTOMER;
}
