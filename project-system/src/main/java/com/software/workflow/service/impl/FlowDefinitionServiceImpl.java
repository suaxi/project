package com.software.workflow.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.exception.BadRequestException;
import com.software.utils.SecurityUtils;
import com.software.workflow.common.constant.ProcessConstant;
import com.software.workflow.common.enums.FlowComment;
import com.software.workflow.dto.FlowProcDefDto;
import com.software.workflow.service.FlowDefinitionService;
import com.software.workflow.service.FlowDeployInsFormService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

/**
 * @author suaxi
 * @date 2024/12/10 20:01
 */
@Service
public class FlowDefinitionServiceImpl implements FlowDefinitionService {

    @Autowired
    private FlowDeployInsFormService deployInsFormService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Override
    public boolean exist(String processDefinitionKey) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey);
        return processDefinitionQuery.count() > 0L;
    }

    @Override
    public Page<FlowProcDefDto> list(String name, Integer pageNum, Integer pageSize) {
        Page<FlowProcDefDto> page = new Page<>(pageNum, pageSize);
        return deployInsFormService.queryProcDefPage(page, name);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importFile(String name, String category, InputStream in) {
        Deployment deploy = repositoryService.createDeployment()
                .addInputStream(name + ProcessConstant.BPMN_FILE_SUFFIX, in)
                .name(name)
                .category(category)
                .deploy();

        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploy.getId())
                .singleResult();

        repositoryService.setProcessDefinitionCategory(definition.getId(), category);
    }

    @Override
    public String readXml(String deployId) throws IOException {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployId)
                .singleResult();
        if (processDefinition == null) {
            throw new BadRequestException("未查询到对应的流程定义信息！");
        }

        InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
    }

    @Override
    public InputStream readImage(String deployId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployId)
                .singleResult();
        if (processDefinition == null) {
            throw new BadRequestException("未查询到对应的流程定义信息！");
        }

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        if (bpmnModel != null) {
            return new DefaultProcessDiagramGenerator().generateDiagram(
                    bpmnModel,
                    "png",
                    Collections.emptyList(),
                    Collections.emptyList(),
                    "宋体",
                    "宋体",
                    "宋体",
                    null,
                    1.0,
                    false);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startProcessInstanceById(String procDefId, Map<String, Object> variables) {
        if (StringUtils.isEmpty(procDefId)) {
            throw new BadRequestException("流程定义ID不能为空！");
        }

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(procDefId)
                .latestVersion()
                .singleResult();

        if (processDefinition != null && processDefinition.isSuspended()) {
            throw new BadRequestException("流程已被挂起,请先激活流程！");
        }

        //设置流程发起人id
        String userId = SecurityUtils.getCurrentUserId().toString();
        identityService.setAuthenticatedUserId(userId);
        variables.put(ProcessConstant.PROCESS_INITIATOR, userId);

        //流程发起时跳过发起人节点
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(procDefId, variables);
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstance.getProcessInstanceId())
                .singleResult();
        if (task != null) {
            taskService.addComment(task.getId(), processInstance.getProcessInstanceId(), FlowComment.NORMAL.getType(), SecurityUtils.getCurrentUsername() + "发起流程申请");
            taskService.complete(task.getId(), variables);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateState(Integer state, String deployId) {
        if (state == null) {
            throw new BadRequestException("状态不能为空！");
        }

        if (StringUtils.isEmpty(deployId)) {
            throw new BadRequestException("流程部署ID不能为空！");
        }

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployId)
                .singleResult();
        if (processDefinition == null) {
            throw new BadRequestException("未查询到对应的流程定义信息！");
        }

        //激活
        if (state == 1) {
            repositoryService.activateProcessDefinitionById(processDefinition.getId(), true, null);
        }

        //挂起
        if (state == 2) {
            repositoryService.suspendProcessDefinitionById(processDefinition.getId(), true, null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String deployId) {
        //级联删除
        repositoryService.deleteDeployment(deployId, true);
    }

}
