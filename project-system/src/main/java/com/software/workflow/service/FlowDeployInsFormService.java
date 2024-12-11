package com.software.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.workflow.dto.FlowProcDefDto;
import com.software.workflow.entity.FlowDeployInsForm;

import java.util.List;

/**
 * @author suaxi
 * @date 2024/12/10 20:19
 */
public interface FlowDeployInsFormService {

    /**
     * 新增
     *
     * @param flowDeployInsForm 流程部署表单关联信息
     * @return
     */
    boolean add(FlowDeployInsForm flowDeployInsForm);

    /**
     * 修改
     *
     * @param flowDeployInsForm 流程部署表单关联信息
     * @return
     */
    boolean update(FlowDeployInsForm flowDeployInsForm);

    /**
     * 批量删除
     *
     * @param ids ids
     * @return
     */
    boolean delete(List<Integer> ids);

    /**
     * 根据ID查询
     *
     * @param id id
     * @return
     */
    FlowDeployInsForm queryById(Integer id);

    /**
     * 查询流程部署实例关联表单列表
     *
     * @param flowDeployInsForm 流程部署表单关联信息
     * @return
     */
    List<FlowDeployInsForm> queryList(FlowDeployInsForm flowDeployInsForm);

    /**
     * 根据流程实例部署ID查询
     *
     * @param deployIds 流程实例部署ID
     * @return
     */
    List<FlowDeployInsForm> queryByDeployIds(List<String> deployIds);

    /**
     * 流程定义分页查询
     *
     * @param page 分页参数
     * @param name 名称
     * @return
     */
    Page<FlowProcDefDto> queryProcDefPage(Page<FlowProcDefDto> page, String name);

}
