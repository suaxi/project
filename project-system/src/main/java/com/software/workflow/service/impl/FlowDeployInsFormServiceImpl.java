package com.software.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.workflow.dto.FlowProcDefDto;
import com.software.workflow.entity.FlowDeployInsForm;
import com.software.workflow.mapper.FlowDeployInsFormMapper;
import com.software.workflow.service.FlowDeployInsFormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author suaxi
 * @date 2024/12/10 21:11
 */
@Service
public class FlowDeployInsFormServiceImpl extends ServiceImpl<FlowDeployInsFormMapper, FlowDeployInsForm> implements FlowDeployInsFormService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(FlowDeployInsForm flowDeployInsForm) {
        LambdaQueryWrapper<FlowDeployInsForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowDeployInsForm::getFormId, flowDeployInsForm.getFormId());
        queryWrapper.eq(FlowDeployInsForm::getDeployId, flowDeployInsForm.getDeployId());
        return this.saveOrUpdate(flowDeployInsForm, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(FlowDeployInsForm flowDeployInsForm) {
        return this.updateById(flowDeployInsForm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Integer> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public FlowDeployInsForm queryById(Integer id) {
        return this.getById(id);
    }

    @Override
    public List<FlowDeployInsForm> queryList(FlowDeployInsForm flowDeployInsForm) {
        LambdaQueryWrapper<FlowDeployInsForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(flowDeployInsForm.getId() != null, FlowDeployInsForm::getId, flowDeployInsForm.getId());
        queryWrapper.eq(flowDeployInsForm.getFormId() != null, FlowDeployInsForm::getFormId, flowDeployInsForm.getFormId());
        queryWrapper.eq(flowDeployInsForm.getDeployId() != null, FlowDeployInsForm::getDeployId, flowDeployInsForm.getDeployId());
        return this.list(queryWrapper);
    }

    @Override
    public List<FlowDeployInsForm> queryByDeployIds(List<String> deployIds) {
        LambdaQueryWrapper<FlowDeployInsForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FlowDeployInsForm::getDeployId, deployIds);
        return this.list(queryWrapper);
    }

    @Override
    public Page<FlowProcDefDto> queryProcDefPage(Page<FlowProcDefDto> page, String name) {
        return this.baseMapper.queryProcDefPage(page, name);
    }

}
