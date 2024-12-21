package com.software.workflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.dto.QueryRequest;
import com.software.utils.SecurityUtils;
import com.software.workflow.entity.FlowListener;
import com.software.workflow.mapper.FlowListenerMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author suaxi
 * @date 2024/12/21 17:49
 */
@Service
public class FlowListenerServiceImpl extends ServiceImpl<FlowListenerMapper, FlowListener> implements FlowListenerService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(FlowListener flowListener) {
        flowListener.setStatus(1);
        flowListener.setCreateUser(SecurityUtils.getCurrentUsername());
        return this.save(flowListener);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(FlowListener flowListener) {
        flowListener.setStatus(1);
        flowListener.setUpdateUser(SecurityUtils.getCurrentUsername());
        return this.updateById(flowListener);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Integer> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public FlowListener findById(Integer id) {
        return this.getById(id);
    }

    @Override
    public List<FlowListener> queryList(FlowListener flowListener) {
        LambdaQueryWrapper<FlowListener> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(flowListener.getName()), FlowListener::getName, flowListener.getName());
        queryWrapper.eq(flowListener.getType() != null, FlowListener::getType, flowListener.getType());
        return this.list(queryWrapper);
    }

    @Override
    public Page<FlowListener> queryPage(FlowListener flowListener, QueryRequest queryRequest) {
        Page<FlowListener> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        LambdaQueryWrapper<FlowListener> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(flowListener.getName()), FlowListener::getName, flowListener.getName());
        queryWrapper.eq(flowListener.getType() != null, FlowListener::getType, flowListener.getType());
        return this.page(page, queryWrapper);
    }

}
