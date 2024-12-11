package com.software.workflow.service.impl;

import com.software.exception.BadRequestException;
import com.software.utils.SecurityUtils;
import com.software.workflow.common.constant.ProcessConstant;
import com.software.workflow.service.FlowInstanceService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author suaxi
 * @date 2024/12/10 22:14
 */
@Service
public class FlowInstanceServiceImpl implements FlowInstanceService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IdentityService identityService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stopProcessInstance(String taskId) {
        if (StringUtils.isEmpty(taskId)) {
            throw new BadRequestException("任务ID不能为空！");
        }

        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        if (task != null) {
            runtimeService.deleteProcessInstance(task.getProcessInstanceId(), "停止流程实例");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateState(Integer state, String instanceId) {
        if (state == null) {
            throw new BadRequestException("状态不能为空！");
        }

        if (StringUtils.isEmpty(instanceId)) {
            throw new BadRequestException("流程实例ID不能为空！");
        }

        //激活
        if (state == 1) {
            runtimeService.activateProcessInstanceById(instanceId);
        }

        //挂起
        if (state == 2) {
            runtimeService.suspendProcessInstanceById(instanceId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String instanceId, String deleteReason) {
        if (StringUtils.isEmpty(instanceId)) {
            throw new BadRequestException("流程实例ID不能为空！");
        }

        HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceById(instanceId);
        if (historicProcessInstance.getEndTime() != null) {
            historyService.deleteHistoricProcessInstance(historicProcessInstance.getId());
            return;
        }

        runtimeService.deleteProcessInstance(instanceId, deleteReason);
        historyService.deleteHistoricProcessInstance(instanceId);
    }

    @Override
    public HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId) {
        if (StringUtils.isEmpty(processInstanceId)) {
            throw new BadRequestException("流程实例ID不能为空！");
        }

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (historicProcessInstance == null) {
            throw new BadRequestException("未查询到对应的流程实例信息: " + processInstanceId);
        }
        return historicProcessInstance;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startProcessInstanceById(String procDefId, Map<String, Object> variables) {
        //设置流程发起人id
        String userId = SecurityUtils.getCurrentUserId().toString();
        identityService.setAuthenticatedUserId(userId);

        variables.put(ProcessConstant.PROCESS_INITIATOR, userId);
        variables.put(ProcessConstant.FLOWABLE_SKIP_EXPRESSION_ENABLED, true);
        runtimeService.startProcessInstanceById(procDefId, variables);
        return true;
    }

}
