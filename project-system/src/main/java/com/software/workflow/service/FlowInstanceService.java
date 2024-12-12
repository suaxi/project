package com.software.workflow.service;

import org.flowable.engine.history.HistoricProcessInstance;

import java.util.Map;

/**
 * @author suaxi
 * @date 2024/12/10 20:03
 */
public interface FlowInstanceService {

    /**
     * 激活或挂起流程实例
     *
     * @param state     状态
     * @param procInsId 流程实例ID
     */
    void updateState(Integer state, String procInsId);

    /**
     * 删除流程
     *
     * @param procInsId    流程实例ID
     * @param deleteReason 删除原因
     */
    void delete(String procInsId, String deleteReason);

    /**
     * 根据流程实例ID查询历史实例数据
     *
     * @param procInsId 流程实例ID
     * @return
     */
    HistoricProcessInstance getHistoricProcessInstanceById(String procInsId);

    /**
     * 根据流程定义ID启动流程实例
     *
     * @param procDefId 流程定义Id
     * @param variables 流程变量
     * @return
     */
    boolean startProcessInstanceById(String procDefId, Map<String, Object> variables);

}
