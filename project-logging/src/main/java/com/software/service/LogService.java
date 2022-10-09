package com.software.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.dto.QueryRequest;
import com.software.entity.Log;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/9 22:04
 */
public interface LogService {

    /**
     * 新增
     *
     * @param log 日志信息
     * @return
     */
    @Async
    void add(Log log);


    /**
     * 删除
     *
     * @param ids ids
     */
    boolean delete(Long[] ids);

    /**
     * 查询日志列表
     *
     * @return
     */
    List<Log> queryList();

    /**
     * 分页查询
     *
     * @param log          日志信息
     * @param queryRequest 查询参数
     * @return
     */
    Page<Log> queryPage(Log log, QueryRequest queryRequest);
}
