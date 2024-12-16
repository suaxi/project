package com.software.workflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.exception.BadRequestException;
import com.software.system.entity.User;
import com.software.system.service.UserService;
import com.software.utils.SecurityUtils;
import com.software.workflow.common.constant.ProcessConstant;
import com.software.workflow.common.enums.FlowComment;
import com.software.workflow.dto.*;
import com.software.workflow.entity.FlowDeployInsForm;
import com.software.workflow.entity.FlowForm;
import com.software.workflow.service.FlowDeployInsFormService;
import com.software.workflow.service.FlowFormService;
import com.software.workflow.service.FlowTaskService;
import com.software.workflow.utils.CustomProcessDiagramGenerator;
import com.software.workflow.utils.FindNextNodeUtil;
import com.software.workflow.utils.FlowableUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.impl.cmd.AddMultiInstanceExecutionCmd;
import org.flowable.engine.impl.cmd.DeleteMultiInstanceExecutionCmd;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author suaxi
 * @date 2024/12/11 16:13
 */
@Service
public class FlowTaskServiceImpl implements FlowTaskService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private UserService userService;

    @Autowired
    private FlowDeployInsFormService flowDeployInsFormService;

    @Autowired
    private FlowFormService flowFormService;

    @Autowired
    private ProcessEngine processEngine;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean complete(FlowTaskQueryDto flowTaskQueryDto) {
        Task task = taskService.createTaskQuery()
                .taskId(flowTaskQueryDto.getTaskId())
                .singleResult();
        if (task == null) {
            throw new BadRequestException("未查询到对应的任务信息！");
        }

        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            taskService.addComment(flowTaskQueryDto.getTaskId(), flowTaskQueryDto.getInstanceId(), FlowComment.DELEGATE.getType(), flowTaskQueryDto.getComment());
            taskService.resolveTask(flowTaskQueryDto.getTaskId(), flowTaskQueryDto.getVariables());
        } else {
            taskService.addComment(flowTaskQueryDto.getTaskId(), flowTaskQueryDto.getInstanceId(), FlowComment.NORMAL.getType(), flowTaskQueryDto.getComment());
            String userId = SecurityUtils.getCurrentUserId().toString();
            taskService.setAssignee(flowTaskQueryDto.getTaskId(), userId);
            taskService.complete(flowTaskQueryDto.getTaskId(), flowTaskQueryDto.getVariables());
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectTask(FlowTaskQueryDto flowTaskQueryDto) {
        Task task = taskService.createTaskQuery()
                .taskId(flowTaskQueryDto.getTaskId())
                .singleResult();
        if (task == null) {
            throw new BadRequestException("未查询到对应的任务信息！");
        }
        if (task.isSuspended()) {
            throw new BadRequestException("任务处于挂起状态！");
        }

        //流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId())
                .singleResult();

        //节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId())
                .getProcesses()
                .get(0);

        //全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        //当前任务节点元素
        FlowElement source = null;
        if (allElements != null && allElements.size() > 0) {
            for (FlowElement flowElement : allElements) {
                //用户任务
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = flowElement;
                }
            }
        }

        //获取所有跳转到的节点 targetIds
        //获取当前节点的所有父级用户任务节点
        List<UserTask> parentUserTaskList = FlowableUtils.iteratorFindParentUserTasks(source, null, null);
        if (parentUserTaskList == null || parentUserTaskList.size() == 0) {
            throw new BadRequestException("当前节点为初始任务节点，不能驳回！");
        }

        //活动ID（节点Key）
        List<String> parentUserTaskKeyList = new ArrayList<>();
        for (UserTask userTask : parentUserTaskList) {
            parentUserTaskKeyList.add(userTask.getId());
        }

        //全部历史节点活动实例
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .orderByHistoricTaskInstanceStartTime()
                .asc()
                .list();

        //过滤回滚导致的脏数据
        List<String> lastHistoricTaskInstanceList = FlowableUtils.historicTaskInstanceClean(allElements, historicTaskInstanceList);
        List<String> targetIds = new ArrayList<>();
        //循环结束标志位，遇到当前目标节点的次数
        int num = 0;
        StringBuilder parentHistoricTaskKey = new StringBuilder();
        for (String historicTaskInstanceKey : lastHistoricTaskInstanceList) {
            //会签或其他特殊情况时，过滤都是同一个节点历史数据的情况
            if (parentHistoricTaskKey.toString().equals(historicTaskInstanceKey)) {
                continue;
            }

            parentHistoricTaskKey = new StringBuilder(historicTaskInstanceKey);
            if (historicTaskInstanceKey.equals(task.getTaskDefinitionKey())) {
                num++;
            }

            // 在数据清洗后，历史节点就是唯一一条从起始到当前节点的历史记录，理论上每个点只会出现一次
            // 在流程中如果出现循环，那么每次循环中间的点也只会出现一次，再出现就是下次循环
            // number == 1，第一次遇到当前节点
            // number == 2，第二次遇到，代表最后一次的循环范围
            if (num == 2) {
                break;
            }

            // 如果当前历史节点，属于父级的节点，说明最后一次经过了这个点，需要退回到这个节点
            if (parentUserTaskKeyList.contains(historicTaskInstanceKey)) {
                targetIds.add(historicTaskInstanceKey);
            }
        }

        //获取所有需要被跳转的节点 currentIds
        //取其中一个父级任务，因为后续要么存在公共网关，要么就是串行公共线路
        UserTask oneUserTask = parentUserTaskList.get(0);
        //获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery()
                .processInstanceId(task.getProcessInstanceId())
                .list();

        List<String> runTaskKeyList = new ArrayList<>();
        for (Task currentTask : runTaskList) {
            runTaskKeyList.add(currentTask.getTaskDefinitionKey());
        }

        //需驳回的任务列表
        List<String> currentIds = new ArrayList<>();
        //通过父级网关的出口连线，结合 runTaskList 比对，获取需要驳回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(oneUserTask, runTaskKeyList, null, null);
        for (UserTask currentUserTask : currentUserTaskList) {
            currentIds.add(currentUserTask.getId());
        }

        //规定：并行网关之前节点必须需存在唯一用户任务节点，如果出现多个任务节点，则并行网关节点默认为结束节点，原因为不考虑多对多情况
        if (targetIds.size() > 1 && currentIds.size() > 1) {
            throw new BadRequestException("任务出现多对多情况，无法驳回！");
        }

        //循环获取需要被驳回的节点的ID，设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        for (String currentId : currentIds) {
            for (Task currentTask : runTaskList) {
                if (currentId.equals(currentTask.getTaskDefinitionKey())) {
                    currentTaskIds.add(currentTask.getId());
                }
            }
        }

        //设置驳回意见
        for (String currentTaskId : currentTaskIds) {
            taskService.addComment(currentTaskId, task.getProcessInstanceId(), FlowComment.REJECT.getType(), flowTaskQueryDto.getComment());
        }

        //父级任务大于一个时，说明当前节点不是并行节点
        if (targetIds.size() > 1) {
            //一对多任务跳转，currentIds 当前节点（一个），targetIds 跳转到的节点（多个）
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
                    .moveSingleActivityIdToActivityIds(currentIds.get(0), targetIds)
                    .changeState();
        }

        //如果父级任务只有一个，当前任务可能为网关中的任务
        if (targetIds.size() == 1) {
            //一对一/多对一的情况，currentIds 当前要跳转的节点列表（一个或多个），targetIds.get(0) 跳转到的节点（一个）
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
                    .moveActivityIdsToSingleActivityId(currentIds, targetIds.get(0))
                    .changeState();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returnTask(FlowTaskQueryDto flowTaskQueryDto) {
        Task task = taskService.createTaskQuery()
                .taskId(flowTaskQueryDto.getTaskId())
                .singleResult();
        if (task == null) {
            throw new BadRequestException("未查询到对应的任务信息！");
        }
        if (task.isSuspended()) {
            throw new BadRequestException("任务处于挂起状态！");
        }

        //流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId())
                .singleResult();

        //节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        //获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        //获取当前任务节点元素
        FlowElement source = null;
        //获取跳转的节点元素
        FlowElement target = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                //当前任务节点元素
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = flowElement;
                }
                //跳转节点元素
                if (flowElement.getId().equals(flowTaskQueryDto.getTargetKey())) {
                    target = flowElement;
                }
            }
        }

        //从当前节点向前扫描
        //如果存在路线上不存在目标节点，说明目标节点是在网关上或非同一路线上，不可跳转
        //否则目标节点相对于当前节点，属于串行
        if (!FlowableUtils.iteratorCheckSequentialReferTarget(source, flowTaskQueryDto.getTargetKey(), null, null)) {
            throw new BadRequestException("当前节点相对于目标节点，不属于串行关系，无法回退！");
        }

        //获取所有正常进行的任务节点Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = new ArrayList<>();
        for (Task currentTask : runTaskList) {
            runTaskKeyList.add(currentTask.getTaskDefinitionKey());
        }

        //需退回的任务列表
        List<String> currentIds = new ArrayList<>();
        //通过父级网关的出口连线，结合runTaskList比对，获取需要退回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(target, runTaskKeyList, null, null);
        for (UserTask currentUserTask : currentUserTaskList) {
            currentIds.add(currentUserTask.getId());
        }

        //循环获取需要被退回的节点ID，设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        for (String currentId : currentIds) {
            for (Task currentTask : runTaskList) {
                if (currentId.equals(currentTask.getTaskDefinitionKey())) {
                    currentTaskIds.add(currentTask.getId());
                }
            }
        }

        //设置退回意见
        for (String currentTaskId : currentTaskIds) {
            taskService.addComment(currentTaskId, task.getProcessInstanceId(), FlowComment.REBACK.getType(), flowTaskQueryDto.getComment());
        }

        //一对一/多对一的情况，currentIds 当前要跳转的节点列表（一个或多个），targetKey 跳转到的节点（一个）
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(task.getProcessInstanceId())
                .moveActivityIdsToSingleActivityId(currentIds, flowTaskQueryDto.getTargetKey())
                .changeState();
    }

    @Override
    public List<UserTask> findReturnTaskList(FlowTaskQueryDto flowTaskQueryDto) {
        Task task = taskService.createTaskQuery()
                .taskId(flowTaskQueryDto.getTaskId())
                .singleResult();
        if (task == null) {
            throw new BadRequestException("未查询到对应的任务信息！");
        }

        //获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId())
                .singleResult();

        //获取所有节点信息，暂不考虑子流程情况
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        Collection<FlowElement> flowElements = process.getFlowElements();
        //获取当前任务节点元素
        UserTask source = null;
        if (flowElements != null) {
            for (FlowElement flowElement : flowElements) {
                //用户任务
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = (UserTask) flowElement;
                }
            }
        }

        //获取节点的所有路线
        List<List<UserTask>> roads = FlowableUtils.findRoad(source, null, null, null);
        //可回退的节点列表
        List<UserTask> userTaskList = new ArrayList<>();
        for (List<UserTask> road : roads) {
            if (userTaskList.size() == 0) {
                //还没有可回退的节点则直接添加
                userTaskList = road;
            } else {
                //如果已有回退节点，则取交集部分
                userTaskList.retainAll(road);
            }
        }
        return userTaskList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTask(FlowTaskQueryDto flowTaskQueryDto) {
        taskService.deleteTask(flowTaskQueryDto.getTaskId(), flowTaskQueryDto.getComment());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claim(FlowTaskQueryDto flowTaskQueryDto) {
        taskService.claim(flowTaskQueryDto.getTaskId(), flowTaskQueryDto.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unClaim(FlowTaskQueryDto flowTaskQueryDto) {
        taskService.unclaim(flowTaskQueryDto.getTaskId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delegateTask(FlowTaskQueryDto flowTaskQueryDto) {
        taskService.delegateTask(flowTaskQueryDto.getTaskId(), flowTaskQueryDto.getAssignee());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resolveTask(FlowTaskQueryDto flowTaskQueryDto) {
        taskService.resolveTask(flowTaskQueryDto.getTaskId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignTask(FlowTaskQueryDto flowTaskQueryDto) {
        taskService.setAssignee(flowTaskQueryDto.getTaskId(), flowTaskQueryDto.getAssignee());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMultiInstanceExecution(FlowTaskQueryDto flowTaskQueryDto) {
        managementService.executeCommand(new AddMultiInstanceExecutionCmd(flowTaskQueryDto.getDefId(), flowTaskQueryDto.getInstanceId(), flowTaskQueryDto.getVariables()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMultiInstanceExecution(FlowTaskQueryDto flowTaskQueryDto) {
        managementService.executeCommand(new DeleteMultiInstanceExecutionCmd(flowTaskQueryDto.getCurrentChildExecutionId(), flowTaskQueryDto.getFlag()));
    }

    @Override
    public Page<FlowTaskDto> myProcess(FlowQueryDto flowQueryDto) {
        Page<FlowTaskDto> page = new Page<>();
        String userId = SecurityUtils.getCurrentUserId().toString();

        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
                .startedBy(userId)
                .orderByProcessInstanceStartTime()
                .desc();

        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery.listPage(flowQueryDto.getPageSize() * (flowQueryDto.getPageNum() - 1), flowQueryDto.getPageSize());
        page.setTotal(historicProcessInstanceQuery.count());
        List<FlowTaskDto> flowTaskDtoList = new ArrayList<>();
        for (HistoricProcessInstance hisIns : historicProcessInstances) {
            FlowTaskDto flowTaskDto = new FlowTaskDto();
            flowTaskDto.setProcInsId(hisIns.getId());
            flowTaskDto.setCreateTime(hisIns.getStartTime());
            flowTaskDto.setCompleteTime(hisIns.getEndTime());

            if (hisIns.getEndTime() != null) {
                flowTaskDto.setDuration(timeMillisParseDate(hisIns.getEndTime().getTime() - hisIns.getStartTime().getTime()));
            } else {
                flowTaskDto.setDuration(timeMillisParseDate(System.currentTimeMillis() - hisIns.getStartTime().getTime()));
            }

            //流程定义信息
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(hisIns.getProcessDefinitionId())
                    .singleResult();

            flowTaskDto.setDeployId(processDefinition.getDeploymentId());
            flowTaskDto.setProcDefName(processDefinition.getName());
            flowTaskDto.setProcDefVersion(processDefinition.getVersion());
            flowTaskDto.setCategory(processDefinition.getCategory());
            flowTaskDto.setProcDefVersion(processDefinition.getVersion());

            //当前所处流程
            List<Task> currentTaskList = taskService.createTaskQuery()
                    .processInstanceId(hisIns.getId())
                    .list();
            if (currentTaskList != null && currentTaskList.size() > 0) {
                flowTaskDto.setTaskId(currentTaskList.get(0).getId());
                flowTaskDto.setTaskName(currentTaskList.get(0).getName());
                if (StringUtils.isNotEmpty(currentTaskList.get(0).getAssignee())) {
                    //当前任务节点经办人信息
                    User user = userService.queryByName(currentTaskList.get(0).getAssignee());
                    if (user != null) {
                        flowTaskDto.setAssigneeId(user.getId());
                        flowTaskDto.setAssigneeName(user.getNickName());
                        flowTaskDto.setAssigneeDeptName(user.getDeptName());
                    }
                }
            } else {
                List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                        .processInstanceId(hisIns.getId())
                        .orderByHistoricTaskInstanceEndTime()
                        .desc()
                        .list();

                flowTaskDto.setTaskId(historicTaskInstanceList.get(0).getId());
                flowTaskDto.setTaskName(historicTaskInstanceList.get(0).getName());
                if (StringUtils.isNotEmpty(historicTaskInstanceList.get(0).getAssignee())) {
                    //当前任务节点经办人信息
                    User user = userService.queryByName(historicTaskInstanceList.get(0).getAssignee());
                    if (user != null) {
                        flowTaskDto.setAssigneeId(user.getId());
                        flowTaskDto.setAssigneeName(user.getNickName());
                        flowTaskDto.setAssigneeDeptName(user.getDeptName());
                    }
                }
            }
            flowTaskDtoList.add(flowTaskDto);
        }
        page.setRecords(flowTaskDtoList);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean stopProcess(FlowTaskQueryDto flowTaskQueryDto) {
        List<Task> taskList = taskService.createTaskQuery()
                .processInstanceId(flowTaskQueryDto.getInstanceId())
                .list();
        if (taskList == null || taskList.size() == 0) {
            throw new BadRequestException("未查询到对应的任务信息，操作失败！");
        }

        //获取当前流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(flowTaskQueryDto.getInstanceId())
                .singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        if (bpmnModel != null) {
            Process process = bpmnModel.getMainProcess();
            List<EndEvent> endNodes = process.findFlowElementsOfType(EndEvent.class, false);
            if (endNodes != null && endNodes.size() > 0) {
                //获取当前流程最后一个节点
                String endId = endNodes.get(0).getId();
                List<Execution> executions = runtimeService.createExecutionQuery()
                        .parentId(processInstance.getProcessInstanceId())
                        .list();
                List<String> executionIds = new ArrayList<>();
                for (Execution execution : executions) {
                    executionIds.add(execution.getId());
                }
                //变更流程为已结束状态
                runtimeService.createChangeActivityStateBuilder()
                        .moveExecutionsToSingleActivityId(executionIds, endId)
                        .changeState();
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean revokeProcess(FlowTaskQueryDto flowTaskQueryDto) {
        //TODO
        return false;
    }

    @Override
    public Page<FlowTaskDto> todoList(FlowQueryDto flowQueryDto) {
        Page<FlowTaskDto> page = new Page<>();
        String userId = SecurityUtils.getCurrentUserId().toString();

        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                .taskCandidateOrAssigned(userId)
                .orderByTaskCreateTime()
                .desc();

        if (StringUtils.isNotEmpty(flowQueryDto.getName())) {
            taskQuery.processDefinitionNameLike(flowQueryDto.getName());
        }

        page.setTotal(taskQuery.count());
        List<Task> taskList = taskQuery.listPage(flowQueryDto.getPageSize() * (flowQueryDto.getPageNum() - 1), flowQueryDto.getPageSize());
        List<FlowTaskDto> flowTaskDtoList = new ArrayList<>();
        for (Task task : taskList) {
            FlowTaskDto flowTaskDto = new FlowTaskDto();
            //当前流程信息
            flowTaskDto.setTaskId(task.getId());
            flowTaskDto.setTaskDefKey(task.getTaskDefinitionKey());
            flowTaskDto.setCreateTime(task.getCreateTime());
            flowTaskDto.setProcDefId(task.getProcessDefinitionId());
            flowTaskDto.setExecutionId(task.getExecutionId());
            flowTaskDto.setTaskName(task.getName());

            //流程定义信息
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(task.getProcessDefinitionId())
                    .singleResult();
            flowTaskDto.setDeployId(processDefinition.getDeploymentId());
            flowTaskDto.setProcDefName(processDefinition.getName());
            flowTaskDto.setProcDefVersion(processDefinition.getVersion());
            flowTaskDto.setProcInsId(task.getProcessInstanceId());

            //流程发起人信息
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            User user = userService.queryById(Integer.valueOf(historicProcessInstance.getStartUserId()));
            flowTaskDto.setStartUserId(user.getId().toString());
            flowTaskDto.setStartUserName(user.getNickName());
            flowTaskDto.setStartDeptName(user.getDeptName());
            flowTaskDtoList.add(flowTaskDto);
        }

        page.setRecords(flowTaskDtoList);
        return page;
    }

    @Override
    public Page<FlowTaskDto> finishedList(FlowQueryDto flowQueryDto) {
        Page<FlowTaskDto> page = new Page<>();
        String userId = SecurityUtils.getCurrentUserId().toString();
        HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .includeProcessVariables()
                .finished()
                .taskAssignee(userId)
                .orderByHistoricTaskInstanceEndTime()
                .desc();

        List<HistoricTaskInstance> historicTaskInstanceList = taskInstanceQuery.listPage(flowQueryDto.getPageSize() * (flowQueryDto.getPageNum() - 1), flowQueryDto.getPageSize());
        List<FlowTaskDto> historicTaskList = new ArrayList<>();
        for (HistoricTaskInstance histTaskIns : historicTaskInstanceList) {
            FlowTaskDto flowTaskDto = new FlowTaskDto();
            //当前流程信息
            flowTaskDto.setTaskId(histTaskIns.getId());

            //经办人信息
            flowTaskDto.setCreateTime(histTaskIns.getCreateTime());
            flowTaskDto.setCompleteTime(histTaskIns.getEndTime());
            flowTaskDto.setDuration(timeMillisParseDate(histTaskIns.getDurationInMillis()));
            flowTaskDto.setProcDefId(histTaskIns.getProcessDefinitionId());
            flowTaskDto.setTaskDefKey(histTaskIns.getTaskDefinitionKey());
            flowTaskDto.setTaskName(histTaskIns.getName());

            //流程定义信息
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(histTaskIns.getProcessDefinitionId())
                    .singleResult();
            flowTaskDto.setDeployId(pd.getDeploymentId());
            flowTaskDto.setProcDefName(pd.getName());
            flowTaskDto.setProcDefVersion(pd.getVersion());
            flowTaskDto.setProcInsId(histTaskIns.getProcessInstanceId());
            flowTaskDto.setHisProcInsId(histTaskIns.getProcessInstanceId());

            //流程发起人信息
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(histTaskIns.getProcessInstanceId())
                    .singleResult();
            User user = userService.queryById(Integer.valueOf(historicProcessInstance.getStartUserId()));
            flowTaskDto.setStartUserId(user.getId().toString());
            flowTaskDto.setStartUserName(user.getNickName());
            flowTaskDto.setStartDeptName(user.getDeptName());
            historicTaskList.add(flowTaskDto);
        }
        page.setTotal(taskInstanceQuery.count());
        page.setRecords(historicTaskList);
        return page;
    }

    @Override
    public Map<String, Object> flowRecord(String procInsId, String deployId) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isNotEmpty(procInsId)) {
            List<HistoricActivityInstance> list = historyService
                    .createHistoricActivityInstanceQuery()
                    .processInstanceId(procInsId)
                    .orderByHistoricActivityInstanceStartTime()
                    .desc()
                    .list();

            List<FlowTaskDto> historicFlowTaskDtoList = new ArrayList<>();
            for (HistoricActivityInstance histIns : list) {
                if (StringUtils.isNotEmpty(histIns.getTaskId())) {
                    FlowTaskDto flowTaskDto = new FlowTaskDto();
                    flowTaskDto.setTaskId(histIns.getTaskId());
                    flowTaskDto.setTaskName(histIns.getActivityName());
                    flowTaskDto.setCreateTime(histIns.getStartTime());
                    flowTaskDto.setCompleteTime(histIns.getEndTime());
                    if (StringUtils.isNotEmpty(histIns.getAssignee())) {
                        User user = userService.queryById(Integer.valueOf(histIns.getAssignee()));
                        flowTaskDto.setAssigneeId(user.getId());
                        flowTaskDto.setAssigneeName(user.getNickName());
                        flowTaskDto.setDeptName(user.getDeptName());
                    }

                    //流程节点经办人信息
                    List<HistoricIdentityLink> historicIdentityLinkList = historyService.getHistoricIdentityLinksForTask(histIns.getTaskId());
                    StringBuilder stringBuilder = new StringBuilder();
                    for (HistoricIdentityLink historicIdentityLink : historicIdentityLinkList) {
                        //获选人，候选组/角色（多个）
                        if ("candidate".equals(historicIdentityLink.getType())) {
                            if (StringUtils.isNotEmpty(historicIdentityLink.getUserId())) {
                                User user = userService.queryById(Integer.valueOf(historicIdentityLink.getUserId()));
                                stringBuilder.append(user.getNickName()).append(",");
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(stringBuilder)) {
                        flowTaskDto.setCandidate(stringBuilder.substring(0, stringBuilder.length() - 1));
                    }

                    flowTaskDto.setDuration(histIns.getDurationInMillis() == null || histIns.getDurationInMillis() == 0 ? null : timeMillisParseDate(histIns.getDurationInMillis()));

                    //审批意见
                    List<Comment> commentList = taskService.getProcessInstanceComments(histIns.getProcessInstanceId());
                    for (Comment comment : commentList) {
                        if (histIns.getTaskId().equals(comment.getTaskId())) {
                            FlowCommentDto flowCommentDto = new FlowCommentDto();
                            flowCommentDto.setType(comment.getType());
                            flowCommentDto.setComment(comment.getFullMessage());
                            flowTaskDto.setComment(flowCommentDto);
                        }
                    }
                    historicFlowTaskDtoList.add(flowTaskDto);
                }
            }
            result.put("flowList", historicFlowTaskDtoList);
        }

        if (StringUtils.isNotEmpty(deployId)) {
            List<FlowDeployInsForm> flowDeployInsFormList = flowDeployInsFormService.queryByDeployIds(Collections.singletonList(deployId));
            if (flowDeployInsFormList == null || flowDeployInsFormList.size() == 0) {
                throw new BadRequestException("查询失败，请先配置流程表单！");
            }
            FlowForm flowForm = flowFormService.queryByFormId(flowDeployInsFormList.get(0).getFormId());
            result.put("formData", flowForm.getFormContent());
        }
        return result;
    }

    @Override
    public String getTaskForm(String taskId) {
        if (StringUtils.isEmpty(taskId)) {
            throw new BadRequestException("任务ID不能为空！");
        }
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        if (task == null) {
            throw new BadRequestException("未查询到对应的任务信息！");
        }

        FlowForm flowForm = flowFormService.queryByFormId(Integer.valueOf(task.getFormKey()));
        return flowForm.getFormContent();
    }

    @Override
    public InputStream diagram(String procInsId) {
        if (StringUtils.isEmpty(procInsId)) {
            throw new BadRequestException("流程实例ID不能为空！");
        }

        String processDefinitionId;

        //获取当前的流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(procInsId)
                .singleResult();

        //如果流程已经结束，则查询结束节点
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(procInsId)
                    .singleResult();

            processDefinitionId = historicProcessInstance.getProcessDefinitionId();
        } else {
            //如果流程没有结束，则取当前活动节点
            ProcessInstance currentProcessInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(procInsId)
                    .singleResult();
            processDefinitionId = currentProcessInstance.getProcessDefinitionId();
        }

        //查询活动的历史节点数据
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();

        List<String> highLightedFlows = new ArrayList<>();
        List<String> highLightedNodes = new ArrayList<>();
        //高亮线
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            if ("sequenceFlow".equals(historicActivityInstance.getActivityType())) {
                //高亮线
                highLightedFlows.add(historicActivityInstance.getActivityId());
            } else {
                //高亮节点
                highLightedNodes.add(historicActivityInstance.getActivityId());
            }
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration configuration = processEngine.getProcessEngineConfiguration();
        //获取自定义图片生成器
        ProcessDiagramGenerator diagramGenerator = new CustomProcessDiagramGenerator();
        return diagramGenerator.generateDiagram(bpmnModel,
                "png",
                highLightedNodes,
                highLightedFlows,
                configuration.getActivityFontName(),
                configuration.getLabelFontName(),
                configuration.getAnnotationFontName(),
                configuration.getClassLoader(),
                1.0,
                true);
    }

    @Override
    public List<FlowViewerDto> getFlowViewer(String procInsId, String executionId) {
        if (StringUtils.isEmpty(procInsId)) {
            throw new BadRequestException("流程实例ID不能为空！");
        }

        List<FlowViewerDto> flowViewerDtoList = new ArrayList<>();
        FlowViewerDto flowViewerDto;
        //获取任务开始节点
        List<HistoricActivityInstance> startNodeList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .listPage(0, 3);
        for (HistoricActivityInstance historicActivityInstance : startNodeList) {
            if (!"sequenceFlow".equals(historicActivityInstance.getActivityType())) {
                flowViewerDto = new FlowViewerDto();
                flowViewerDto.setKey(historicActivityInstance.getActivityId());
                flowViewerDto.setCompleted(historicActivityInstance.getEndTime() != null);
                flowViewerDtoList.add(flowViewerDto);
            }
        }

        //历史节点
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .executionId(executionId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            if (!"sequenceFlow".equals(historicActivityInstance.getActivityType())) {
                flowViewerDto = new FlowViewerDto();
                flowViewerDto.setKey(historicActivityInstance.getActivityId());
                flowViewerDto.setCompleted(historicActivityInstance.getEndTime() != null);
                flowViewerDtoList.add(flowViewerDto);
            }
        }
        return flowViewerDtoList;
    }

    @Override
    public Map<String, Object> processVariables(String taskId) {
        if (StringUtils.isEmpty(taskId)) {
            throw new BadRequestException("任务ID不能为空！");
        }

        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .includeProcessVariables()
                .finished()
                .taskId(taskId)
                .singleResult();
        if (historicTaskInstance != null) {
            return historicTaskInstance.getProcessVariables();
        } else {
            return taskService.getVariables(taskId);
        }
    }

    @Override
    public FlowNextNodeDto getNextFlowNode(FlowTaskQueryDto flowTaskQueryDto) {
        //获取当前节点并找到下一步节点
        Task task = taskService.createTaskQuery()
                .taskId(flowTaskQueryDto.getTaskId())
                .singleResult();
        if (task == null) {
            throw new BadRequestException("任务不存在或已被审批！");
        }

        //获取当前流程所有流程变量
        Map<String, Object> variables = taskService.getVariables(task.getId());
        List<UserTask> nextUserTaskList = FindNextNodeUtil.getNextUserTasks(repositoryService, task, variables);
        if (nextUserTaskList.size() == 0) {
            return null;
        }
        return this.getFlowAttribute(nextUserTaskList);
    }

    @Override
    public FlowNextNodeDto getNextFlowNodeByStart(FlowTaskQueryDto flowTaskQueryDto) {
        //查询流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(flowTaskQueryDto.getDeploymentId())
                .singleResult();
        if (processDefinition == null) {
            throw new BadRequestException("流程信息不存在！");
        }

        //获取下一任务节点
        List<UserTask> nextUserTaskList = FindNextNodeUtil.getNextUserTasksByStart(repositoryService, processDefinition, flowTaskQueryDto.getVariables());
        if (nextUserTaskList.size() == 0) {
            throw new BadRequestException("暂未查找到下一任务，请检查流程设计是否正确！");
        }
        return this.getFlowAttribute(nextUserTaskList);
    }

    @Override
    public JSONObject flowFormData(String deployId) {
        //第一次申请获取初始化表单
        if (StringUtils.isEmpty(deployId)) {
            throw new BadRequestException("流程部署ID不能为空！");
        }
        List<FlowDeployInsForm> flowDeployInsFormList = flowDeployInsFormService.queryByDeployIds(Collections.singletonList(deployId));
        if (flowDeployInsFormList == null || flowDeployInsFormList.size() == 0) {
            throw new BadRequestException("查询失败，请先配置流程表单！");
        }
        FlowForm flowForm = flowFormService.queryByFormId(flowDeployInsFormList.get(0).getFormId());
        return JSONObject.parseObject(flowForm.getFormContent());
    }

    @Override
    public Map<String, Object> flowXmlAndNode(String procInsId, String deployId) throws IOException {
        if (StringUtils.isEmpty(procInsId)) {
            throw new BadRequestException("流程实例ID不能为空！");
        }

        List<FlowViewerDto> flowViewerList = new ArrayList<>();
        //查询已经完成的节点
        List<HistoricActivityInstance> finishedList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId)
                .finished()
                .list();

        for (HistoricActivityInstance historicActivityInstance : finishedList) {
            FlowViewerDto flowViewerDto = new FlowViewerDto();
            flowViewerDto.setKey(historicActivityInstance.getActivityId());
            flowViewerDto.setCompleted(true);
            //退回节点不进行展示
            if (StringUtils.isEmpty(historicActivityInstance.getDeleteReason())) {
                flowViewerList.add(flowViewerDto);
            }
        }

        //查询待办节点
        List<HistoricActivityInstance> unFinishedList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId)
                .unfinished()
                .list();

        for (HistoricActivityInstance historicActivityInstance : unFinishedList) {
            //删除已退回节点
            flowViewerList.removeIf(task -> task.getKey().equals(historicActivityInstance.getActivityId()));

            FlowViewerDto flowViewerDto = new FlowViewerDto();
            flowViewerDto.setKey(historicActivityInstance.getActivityId());
            flowViewerDto.setCompleted(false);
            flowViewerList.add(flowViewerDto);
        }

        Map<String, Object> result = new HashMap<>();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployId)
                .singleResult();
        InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
        result.put("nodeData", flowViewerList);
        result.put("xmlData", IOUtils.toString(inputStream, StandardCharsets.UTF_8));
        return result;
    }

    @Override
    public Map<String, Object> flowTaskForm(String taskId) {
        if (StringUtils.isEmpty(taskId)) {
            throw new BadRequestException("任务ID不能为空！");
        }

        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        //流程变量
        Map<String, Object> parameters;
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .includeProcessVariables()
                .finished()
                .taskId(taskId)
                .singleResult();
        if (historicTaskInstance != null) {
            parameters = historicTaskInstance.getProcessVariables();
        } else {
            parameters = taskService.getVariables(taskId);
        }

        JSONObject oldVariables = JSONObject.parseObject(JSONObject.toJSONString(parameters.get("formJson")));
        List<JSONObject> oldFields = JSONObject.parseObject(JSONObject.toJSONString(oldVariables.get("widgetList")), new TypeReference<List<JSONObject>>() {
        });
        //设置已填写的表单为禁用状态
        for (JSONObject oldField : oldFields) {
            JSONObject options = oldField.getJSONObject("options");
            options.put("disabled", true);
        }

        if (StringUtils.isNotEmpty(task.getFormKey())) {
            FlowForm flowForm = flowFormService.queryByFormId(Integer.valueOf(task.getFormKey()));
            JSONObject data = JSONObject.parseObject(flowForm.getFormContent());
            List<JSONObject> newFields = JSONObject.parseObject(JSONObject.toJSONString(data.get("widgetList")), new TypeReference<List<JSONObject>>() {
            });
            //表单回显时，加入子表单信息到流程变量中
            for (JSONObject newField : newFields) {
                String key = newField.getString("id");
                //处理图片上传组件回显问题
                if ("picture-upload".equals(newField.getString("type"))) {
                    parameters.put(key, new ArrayList<>());
                } else {
                    parameters.put(key, null);
                }
            }
            oldFields.addAll(newFields);
        }
        oldVariables.put("widgetList", oldFields);
        parameters.put("formJson", oldVariables);
        return parameters;
    }

    @Override
    public FlowTaskDto flowTaskInfo(String procInsId, String elementId) {
        if (StringUtils.isEmpty(procInsId)) {
            throw new BadRequestException("流程实例ID不能为空！");
        }

        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId)
                .activityId(elementId)
                .list();
        //退回任务后有多条数据，只取待办任务进行展示
        historicActivityInstanceList.removeIf(task -> StringUtils.isNotBlank(task.getDeleteReason()));
        if (historicActivityInstanceList.size() == 0) {
            return null;
        }

        if (historicActivityInstanceList.size() > 1) {
            historicActivityInstanceList.removeIf(task -> Objects.nonNull(task.getEndTime()));
        }
        HistoricActivityInstance historicActivityInstance = historicActivityInstanceList.get(0);
        FlowTaskDto flowTaskDto = new FlowTaskDto();
        flowTaskDto.setTaskId(historicActivityInstance.getTaskId());
        flowTaskDto.setTaskName(historicActivityInstance.getActivityName());
        flowTaskDto.setCreateTime(historicActivityInstance.getStartTime());
        flowTaskDto.setCompleteTime(historicActivityInstance.getEndTime());
        if (StringUtils.isNotEmpty(historicActivityInstance.getAssignee())) {
            User user = userService.queryById(Integer.valueOf(historicActivityInstance.getAssignee()));
            flowTaskDto.setAssigneeId(user.getId());
            flowTaskDto.setAssigneeName(user.getNickName());
            flowTaskDto.setDeptName(user.getDeptName());
        }

        //展示审批人员
        List<HistoricIdentityLink> historicIdentityLinkList = historyService.getHistoricIdentityLinksForTask(historicActivityInstance.getTaskId());
        StringBuilder stringBuilder = new StringBuilder();
        historicIdentityLinkList.forEach(historicIdentityLink -> {
            //候选人，候选组/角色（多个）
            if ("candidate".equals(historicIdentityLink.getType())) {
                if (StringUtils.isNotBlank(historicIdentityLink.getUserId())) {
                    User user = userService.queryById(Integer.valueOf(historicIdentityLink.getUserId()));
                    stringBuilder.append(user.getNickName()).append(",");
                }
            }
        });

        if (StringUtils.isNotEmpty(stringBuilder)) {
            flowTaskDto.setCandidate(stringBuilder.substring(0, stringBuilder.length() - 1));
        }

        flowTaskDto.setDuration(historicActivityInstance.getDurationInMillis() == null || historicActivityInstance.getDurationInMillis() == 0 ? null : timeMillisParseDate(historicActivityInstance.getDurationInMillis()));
        //审批意见
        List<Comment> commentList = taskService.getProcessInstanceComments(historicActivityInstance.getProcessInstanceId());
        commentList.forEach(comment -> {
            if (historicActivityInstance.getTaskId().equals(comment.getTaskId())) {
                FlowCommentDto flowCommentDto = new FlowCommentDto();
                flowCommentDto.setType(comment.getType());
                flowCommentDto.setComment(comment.getFullMessage());
                flowTaskDto.setComment(flowCommentDto);
            }
        });
        return flowTaskDto;
    }

    /**
     * 流程完成时间处理
     *
     * @param ms timeMillis
     * @return
     */
    private static String timeMillisParseDate(long ms) {
        long day = ms / (24 * 60 * 60 * 1000);
        long hour = (ms / (60 * 60 * 1000) - day * 24);
        long minute = ((ms / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long second = (ms / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

        if (day > 0) {
            return day + "天" + hour + "小时" + minute + "分钟";
        }
        if (hour > 0) {
            return hour + "小时" + minute + "分钟";
        }
        if (minute > 0) {
            return minute + "分钟";
        }
        if (second > 0) {
            return second + "秒";
        } else {
            return 0 + "秒";
        }
    }

    /**
     * 获取任务节点属性，包含自定义属性等
     *
     * @param nextUserTaskList
     */
    private FlowNextNodeDto getFlowAttribute(List<UserTask> nextUserTaskList) {
        FlowNextNodeDto flowNextNodeDto = new FlowNextNodeDto();
        for (UserTask userTask : nextUserTaskList) {
            MultiInstanceLoopCharacteristics multiInstance = userTask.getLoopCharacteristics();
            //会签节点
            if (Objects.nonNull(multiInstance)) {
                flowNextNodeDto.setVars(multiInstance.getInputDataItem());
                flowNextNodeDto.setType(ProcessConstant.PROCESS_MULTI_INSTANCE);
                flowNextNodeDto.setDataType(ProcessConstant.DYNAMIC);
            } else {
                //读取自定义节点属性，判断是否是否需要动态指定任务接收人员、组
                String dataType = userTask.getAttributeValue(ProcessConstant.NAMASPASE, ProcessConstant.PROCESS_CUSTOM_DATA_TYPE);
                String userType = userTask.getAttributeValue(ProcessConstant.NAMASPASE, ProcessConstant.PROCESS_CUSTOM_USER_TYPE);
                flowNextNodeDto.setVars(ProcessConstant.PROCESS_APPROVAL);
                flowNextNodeDto.setType(userType);
                flowNextNodeDto.setDataType(dataType);
            }
        }
        return flowNextNodeDto;
    }

}
