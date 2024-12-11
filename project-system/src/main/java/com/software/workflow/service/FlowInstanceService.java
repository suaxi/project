package com.software.workflow.service;

import org.flowable.engine.history.HistoricProcessInstance;

import java.util.Map;

/**
 * @author suaxi
 * @date 2024/12/10 20:03
 */
public interface FlowInstanceService {

    /**
     * 结束流程实例
     *
     * @param taskId 任务ID
     */
    void stopProcessInstance(String taskId);

    /**
     * 激活或挂起流程实例
     *
     * @param state      状态
     * @param instanceId 流程实例ID
     */
    void updateState(Integer state, String instanceId);

    /**
     * 删除流程实例ID
     *
     * @param instanceId   流程实例ID
     * @param deleteReason 删除原因
     */
    void delete(String instanceId, String deleteReason);

    /**
     * 根据流程实例ID查询历史实例数据
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
    HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId);

    /**
     * 根据流程定义ID启动流程实例
     *
     * @param procDefId 流程定义Id
     * @param variables 流程变量
     * @return
     */
    boolean startProcessInstanceById(String procDefId, Map<String, Object> variables);

}
