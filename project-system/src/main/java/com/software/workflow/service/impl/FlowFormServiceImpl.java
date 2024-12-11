package com.software.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.exception.BadRequestException;
import com.software.utils.SecurityUtils;
import com.software.workflow.entity.FlowDeployInsForm;
import com.software.workflow.entity.FlowForm;
import com.software.workflow.mapper.FlowFormMapper;
import com.software.workflow.service.FlowDeployInsFormService;
import com.software.workflow.service.FlowFormService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author suaxi
 * @date 2024/12/10 20:34
 */
@Service
public class FlowFormServiceImpl extends ServiceImpl<FlowFormMapper, FlowForm> implements FlowFormService {

    @Autowired
    private FlowDeployInsFormService flowDeployInsFormService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(FlowForm flowForm) {
        flowForm.setCreateUser(SecurityUtils.getCurrentUsername());
        return this.save(flowForm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(FlowForm flowForm) {
        flowForm.setUpdateUser(SecurityUtils.getCurrentUsername());
        return this.updateById(flowForm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Integer> formIds) {
        return this.removeByIds(formIds);
    }

    @Override
    public FlowForm queryByFormId(Integer formId) {
        return this.getById(formId);
    }

    @Override
    public List<FlowForm> queryByFormIds(List<Integer> formIds) {
        LambdaQueryWrapper<FlowForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FlowForm::getFormId, formIds);
        return this.list(queryWrapper);
    }

    @Override
    public List<FlowForm> queryList(FlowForm flowForm) {
        LambdaQueryWrapper<FlowForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(flowForm.getFormId() != null, FlowForm::getFormId, flowForm.getFormId());
        queryWrapper.like(StringUtils.isNotEmpty(flowForm.getFormName()), FlowForm::getFormName, flowForm.getFormName());
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean mountFlowForm(FlowDeployInsForm flowDeployInsForm) {
        FlowForm flowForm = this.getById(flowDeployInsForm.getFormId());
        if (flowForm == null) {
            throw new BadRequestException("未查询到对应的流程表单信息！");
        }
        return flowDeployInsFormService.add(flowDeployInsForm);
    }

}
