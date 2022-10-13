package com.software.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.entity.Job;
import com.software.mapper.JobMapper;
import com.software.service.JobService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:38
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(Job job) {
        //TODO 创建人信息
        return this.save(job);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Job job) {
        return this.updateById(job);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long[] ids) {
        if (ids.length > 0) {
            return this.removeByIds(Arrays.asList(ids));
        }
        return false;
    }

    @Override
    public Job queryById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("岗位id不能为空");
        }
        return this.getById(id);
    }

    @Override
    public Job queryByName(String name) {
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.lambda().eq(Job::getName, name);
        }
        return this.getOne(queryWrapper);
    }

    @Override
    public List<Job> queryList() {
        return this.list();
    }

    @Override
    public Page<Job> queryPage(Job job, QueryRequest queryRequest) {
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(job.getName())) {
            queryWrapper.lambda().eq(Job::getName, job.getName());
        }
        if (StringUtils.isNotBlank(job.getEnabled())) {
            queryWrapper.lambda().eq(Job::getEnabled, job.getEnabled());
        }
        if (job.getSort() != null) {
            queryWrapper.lambda().eq(Job::getSort, job.getSort());
        }
        if (StringUtils.isNotBlank(queryRequest.getOrder())) {
            queryWrapper.orderBy(true, StringConstant.ASC.equals(queryRequest.getOrder()), queryRequest.getField());
        } else {
            queryWrapper.orderBy(true, false, "create_time");
        }
        Page<Job> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.page(page, queryWrapper);
    }
}
