package com.software.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.entity.Log;
import com.software.mapper.LogMapper;
import com.software.service.LogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/9 22:04
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Override
    public void add(Log log) {
        this.save(log);
    }

    @Override
    public boolean delete(Long[] ids) {
        if (ids.length > 0) {
            return this.removeByIds(Arrays.asList(ids));
        }
        return false;
    }

    @Override
    public List<Log> queryList() {
        return this.queryList();
    }

    @Override
    public Page<Log> queryPage(Log log, QueryRequest queryRequest) {
        QueryWrapper<Log> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(log.getLogType())) {
            queryWrapper.lambda().eq(Log::getLogType, log.getLogType());
        }
        if (StringUtils.isNotBlank(log.getUsername())) {
            queryWrapper.lambda().eq(Log::getUsername, log.getUsername());
        }
        if (StringUtils.isNotBlank(log.getCreateTimeFrom()) && StringUtils.isNotBlank(log.getCreateTimeTo())) {
            queryWrapper.lambda()
                    .ge(Log::getCreateTime, log.getCreateTimeFrom())
                    .le(Log::getCreateTime, log.getCreateTimeTo());
        }
        if (StringUtils.isNotBlank(queryRequest.getOrder())) {
            queryWrapper.orderBy(true, StringConstant.ASC.equals(queryRequest.getOrder()), queryRequest.getField());
        } else {
            queryWrapper.orderBy(true, false, "create_time");
        }
        Page<Log> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.page(page, queryWrapper);
    }
}
