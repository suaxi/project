package com.software.workflow.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.workflow.dto.*;
import org.flowable.bpmn.model.UserTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author suaxi
 * @date 2024/12/10 20:05
 */
public interface FlowTaskService {

    /**
     * 审批任务
     *
     * @param flowTaskQueryDto 任务信息
     */
    boolean complete(FlowTaskQueryDto flowTaskQueryDto);

    /**
     * 驳回任务
     *
     * @param flowTaskQueryDto 任务信息
     */
    void rejectTask(FlowTaskQueryDto flowTaskQueryDto);


    /**
     * 退回任务
     *
     * @param flowTaskQueryDto 任务信息
     */
    void returnTask(FlowTaskQueryDto flowTaskQueryDto);

    /**
     * 获取所有可回退的节点
     *
     * @param flowTaskQueryDto 任务信息
     * @return
     */
    List<UserTask> findReturnTaskList(FlowTaskQueryDto flowTaskQueryDto);

    /**
     * 删除任务
     *
     * @param flowTaskQueryDto 任务信息
     */
    void deleteTask(FlowTaskQueryDto flowTaskQueryDto);

    /**
     * 认领/签收任务
     *
     * @param flowTaskQueryDto 任务信息
     */
    void claim(FlowTaskQueryDto flowTaskQueryDto);

    /**
     * 取消认领/签收任务
     *
     * @param flowTaskQueryDto 任务信息
     */
    void unClaim(FlowTaskQueryDto flowTaskQueryDto);

    /**
     * 委派任务
     *
     * @param flowTaskQueryDto 任务信息
     */
    void delegateTask(FlowTaskQueryDto flowTaskQueryDto);

    /**
     * 归还任务
     *
     * @param flowTaskQueryDto 归还任务
     */
    void resolveTask(FlowTaskQueryDto flowTaskQueryDto);


    /**
     * 转办任务
     *
     * @param flowTaskQueryDto 任务信息
     */
    void assignTask(FlowTaskQueryDto flowTaskQueryDto);


    /**
     * 多实例加签
     *
     * @param flowTaskQueryDto 任务信息
     */
    void addMultiInstanceExecution(FlowTaskQueryDto flowTaskQueryDto);

    /**
     * 多实例减签
     *
     * @param flowTaskQueryDto 任务信息
     */
    void deleteMultiInstanceExecution(FlowTaskQueryDto flowTaskQueryDto);

    /**
     * 查询我发起的流程（分页）
     *
     * @param flowQueryDto 流程查询Dto
     * @return
     */
    Page<FlowTaskDto> myProcess(FlowQueryDto flowQueryDto);

    /**
     * 停止流程
     *
     * @param flowTaskQueryDto 任务信息
     * @return
     */
    boolean stopProcess(FlowTaskQueryDto flowTaskQueryDto);

    /**
     * 撤回流程
     *
     * @param flowTaskQueryDto 任务信息
     * @return
     */
    boolean revokeProcess(FlowTaskQueryDto flowTaskQueryDto);

    /**
     * 代办任务列表
     *
     * @param flowQueryDto 流程查询Dto
     * @return
     */
    Page<FlowTaskDto> todoList(FlowQueryDto flowQueryDto);

    /**
     * 已办任务列表
     *
     * @param flowQueryDto 流程查询Dto
     * @return
     */
    Page<FlowTaskDto> finishedList(FlowQueryDto flowQueryDto);

    /**
     * 流程历史流转记录
     *
     * @param procInsId 流程实例Id
     * @return
     */
    Map<String, Object> flowRecord(String procInsId, String deployId);

    /**
     * 根据任务ID查询挂载的表单信息
     *
     * @param taskId 任务Id
     * @return
     */
    String getTaskForm(String taskId);

    /**
     * 获取流程过程图
     *
     * @param procInsId 流程实例ID
     * @return
     */
    InputStream diagram(String procInsId);

    /**
     * 获取流程执行节点
     *
     * @param procInsId 流程实例ID
     * @return
     */
    List<FlowViewerDto> getFlowViewer(String procInsId, String executionId);

    /**
     * 获取流程变量
     *
     * @param taskId 任务ID
     * @return
     */
    Map<String, Object> processVariables(String taskId);

    /**
     * 获取下一节点
     *
     * @param flowTaskQueryDto 任务信息
     * @return
     */
    FlowNextNodeDto getNextFlowNode(FlowTaskQueryDto flowTaskQueryDto);

    /**
     * 发起流程获取下一节点
     *
     * @param flowTaskQueryDto 任务信息Dto
     * @return
     */
    FlowNextNodeDto getNextFlowNodeByStart(FlowTaskQueryDto flowTaskQueryDto);

    /**
     * 根据流程部署ID查询流程初始化表单
     *
     * @param deployId 流程部署ID
     * @return
     */
    JSONObject flowFormData(String deployId);

    /**
     * 流程节点信息
     *
     * @param procInsId 流程实例ID
     * @return
     */
    Map<String, Object> flowXmlAndNode(String procInsId, String deployId) throws IOException;

    /**
     * 流程节点表单
     *
     * @param taskId 任务ID
     * @return
     */
    Map<String, Object> flowTaskForm(String taskId);

    /**
     * 流程节点信息
     *
     * @param procInsId 流程实例ID
     * @param elementId 元素ID
     * @return
     */
    FlowTaskDto flowTaskInfo(String procInsId, String elementId);

}
