package com.software.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.dto.QueryRequest;
import com.software.workflow.entity.FlowListener;

import java.util.List;

/**
 * @author suaxi
 * @date 2024/12/21 17:47
 */
public interface FlowListenerService {

    /**
     * 新增
     *
     * @param flowListener 流程监听
     * @return 结果
     */
    boolean add(FlowListener flowListener);

    /**
     * 修改
     *
     * @param flowListener 流程监听
     * @return 结果
     */
    boolean update(FlowListener flowListener);

    /**
     * 批量删除
     *
     * @param ids ids
     * @return
     */
    boolean delete(List<Integer> ids);

    /**
     * 根据ID查询流程监听
     *
     * @param id id
     * @return
     */
    FlowListener findById(Integer id);

    /**
     * 查询流程监听列表
     *
     * @param flowListener 流程监听
     * @return
     */
    List<FlowListener> queryList(FlowListener flowListener);

    /**
     * 分页查询
     *
     * @param flowListener 流程监听
     * @param queryRequest 查询参数
     * @return
     */
    Page<FlowListener> queryPage(FlowListener flowListener, QueryRequest queryRequest);

}
