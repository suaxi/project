package com.software.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.dto.QueryRequest;
import com.software.workflow.entity.FlowDeployInsForm;
import com.software.workflow.entity.FlowForm;

import java.util.List;

/**
 * @author suaxi
 * @date 2024/12/10 20:27
 */
public interface FlowFormService {

    /**
     * 新增
     *
     * @param flowForm 流程表单信息
     * @return 结果
     */
    boolean add(FlowForm flowForm);

    /**
     * 修改
     *
     * @param flowForm 流程表单信息
     * @return 结果
     */
    boolean update(FlowForm flowForm);

    /**
     * 批量删除
     *
     * @param formIds ids
     * @return
     */
    boolean delete(List<Integer> formIds);

    /**
     * 根据ID查询
     *
     * @param formId 流程表单ID
     * @return
     */
    FlowForm queryByFormId(Integer formId);

    /**
     * 根据ID查询
     *
     * @param formIds 流程表单ID
     * @return
     */
    List<FlowForm> queryByFormIds(List<Integer> formIds);

    /**
     * 查询流程表单列表
     *
     * @param flowForm 流程表单信息
     * @return
     */
    List<FlowForm> queryList(FlowForm flowForm);

    /**
     * 分页查询
     *
     * @param flowForm     表单信息
     * @param queryRequest 查询参数
     * @return
     */
    Page<FlowForm> queryPage(FlowForm flowForm, QueryRequest queryRequest);

    /**
     * 挂载流程表单
     *
     * @param flowDeployInsForm 流程部署实例表单关联信息
     * @return
     */
    boolean mountFlowForm(FlowDeployInsForm flowDeployInsForm);

}
