package com.software.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.dto.QueryRequest;
import com.software.utils.SecurityUtils;
import com.software.workflow.entity.FlowExpression;
import com.software.workflow.mapper.FlowExpressionMapper;
import com.software.workflow.service.FlowExpressionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author suaxi
 * @date 2024/12/19 13:44
 */
@Service
public class FlowExpressionServiceImpl extends ServiceImpl<FlowExpressionMapper, FlowExpression> implements FlowExpressionService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(FlowExpression flowExpression) {
        flowExpression.setStatus(1);
        flowExpression.setCreateUser(SecurityUtils.getCurrentUsername());
        return this.save(flowExpression);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(FlowExpression flowExpression) {
        flowExpression.setUpdateUser(SecurityUtils.getCurrentUsername());
        return this.updateById(flowExpression);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Integer> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public FlowExpression findById(Integer id) {
        return this.getById(id);
    }

    @Override
    public List<FlowExpression> queryList(FlowExpression flowExpression) {
        LambdaQueryWrapper<FlowExpression> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(flowExpression.getName()), FlowExpression::getName, flowExpression.getName());
        queryWrapper.eq(flowExpression.getType() != null, FlowExpression::getType, flowExpression.getType());
        queryWrapper.eq(FlowExpression::getStatus, 1);
        return this.list(queryWrapper);
    }

    @Override
    public Page<FlowExpression> queryPage(FlowExpression flowExpression, QueryRequest queryRequest) {
        Page<FlowExpression> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        LambdaQueryWrapper<FlowExpression> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(flowExpression.getName()), FlowExpression::getName, flowExpression.getName());
        queryWrapper.eq(flowExpression.getType() != null, FlowExpression::getType, flowExpression.getType());
        return this.page(page, queryWrapper);
    }

}
