package com.software.inteceptor;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Wang Hao
 * @date 2022/10/9 22:14
 */
@Slf4j
@Component
public class AutoDateInterceptor implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("MyBatis Plus [ Auto Date Interceptor ] insert...");
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("MyBatis Plus [ Auto Date Interceptor ] update...");
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
