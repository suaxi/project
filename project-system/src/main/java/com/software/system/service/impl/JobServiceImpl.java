package com.software.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.dto.QueryRequest;
import com.software.system.entity.Job;
import com.software.system.mapper.JobMapper;
import com.software.system.service.JobService;
import com.software.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        job.setCreateUser(SecurityUtils.getCurrentUsername());
        return this.save(job);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Job job) {
        job.setUpdateUser(SecurityUtils.getCurrentUsername());
        return this.updateById(job);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Long> ids) {
        if (ids.size() > 0) {
            return this.removeByIds(ids);
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
        return this.list(new LambdaQueryWrapper<Job>().eq(Job::getEnabled, true));
    }

    @Override
    public Page<Job> queryPage(Job job, QueryRequest queryRequest) {
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(job.getName())) {
            queryWrapper.lambda().like(Job::getName, job.getName());
        }
        if (job.getEnabled() != null) {
            queryWrapper.lambda().eq(Job::getEnabled, job.getEnabled());
        }
        Page<Job> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.page(page, queryWrapper);
    }
}
