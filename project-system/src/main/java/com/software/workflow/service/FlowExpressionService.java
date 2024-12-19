package com.software.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.dto.QueryRequest;
import com.software.workflow.entity.FlowExpression;

import java.util.List;

/**
 * @author suaxi
 * @date 2024/12/19 13:43
 */
public interface FlowExpressionService {

    /**
     * 新增
     *
     * @param flowExpression 流程表达式
     * @return 结果
     */
    boolean add(FlowExpression flowExpression);

    /**
     * 修改
     *
     * @param flowExpression 流程表达式
     * @return 结果
     */
    boolean update(FlowExpression flowExpression);

    /**
     * 批量删除
     *
     * @param ids ids
     * @return
     */
    boolean delete(List<Integer> ids);

    /**
     * 根据ID查询流程表达式
     *
     * @param id id
     * @return
     */
    FlowExpression findById(Integer id);

    /**
     * 查询流程表达式列表
     *
     * @param flowExpression 流程表达式
     * @return
     */
    List<FlowExpression> queryList(FlowExpression flowExpression);

    /**
     * 分页查询
     *
     * @param flowExpression 流程表达式
     * @param queryRequest   查询参数
     * @return
     */
    Page<FlowExpression> queryPage(FlowExpression flowExpression, QueryRequest queryRequest);

}
