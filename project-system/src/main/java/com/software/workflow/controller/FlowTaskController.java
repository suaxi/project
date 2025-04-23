package com.software.workflow.controller;

import cn.hutool.http.server.HttpServerResponse;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.exception.BadRequestException;
import com.software.workflow.dto.*;
import com.software.workflow.service.FlowTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.UserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author suaxi
 * @date 2024/12/11 22:09
 */
@Slf4j
@RestController
@RequestMapping("/workflow/task")
@Api(tags = "流程任务接口")
public class FlowTaskController {

    @Autowired
    private FlowTaskService flowTaskService;

    @ApiOperation("我发起的流程")
    @GetMapping("/myProcess")
    public ResponseEntity<Page<FlowTaskDto>> myProcess(FlowQueryDto flowQueryDto) {
        return new ResponseEntity<>(flowTaskService.myProcess(flowQueryDto), HttpStatus.OK);
    }

    @ApiOperation("取消申请")
    @PutMapping("/stop-process")
    @OperationLog("取消申请")
    public ResponseEntity<Boolean> stopProcess(@RequestBody FlowTaskQueryDto flowTaskQueryDto) {
        return new ResponseEntity<>(flowTaskService.stopProcess(flowTaskQueryDto), HttpStatus.OK);
    }

    @ApiOperation("待办任务（分页）")
    @GetMapping("/todo-list")
    public ResponseEntity<Page<FlowTaskDto>> todoList(FlowQueryDto flowQueryDto) {
        return new ResponseEntity<>(flowTaskService.todoList(flowQueryDto), HttpStatus.OK);
    }

    @ApiOperation("已办任务（分页）")
    @GetMapping("/finished-list")
    public ResponseEntity<Page<FlowTaskDto>> finishedList(FlowQueryDto flowQueryDto) {
        return new ResponseEntity<>(flowTaskService.finishedList(flowQueryDto), HttpStatus.OK);
    }

    @ApiOperation("流程历史流转记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "procInsId", value = "流程实例ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deployId", value = "流程部署ID", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping("/flow-record")
    public ResponseEntity<Map<String, Object>> flowRecord(@RequestParam("procInsId") String procInsId, @RequestParam("deployId") String deployId) {
        return new ResponseEntity<>(flowTaskService.flowRecord(procInsId, deployId), HttpStatus.OK);
    }

    @ApiOperation("根据任务ID查询挂载的表单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping("/task-form")
    public ResponseEntity<String> taskForm(@RequestParam("taskId") String taskId) {
        return new ResponseEntity<>(flowTaskService.getTaskForm(taskId), HttpStatus.OK);
    }

    @ApiOperation("根据流程部署ID查询流程初始化表单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deployId", value = "流程部署ID", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping("/flow-form-data")
    public ResponseEntity<JSONObject> flowFormData(@RequestParam("deployId") String deployId) {
        return new ResponseEntity<>(flowTaskService.flowFormData(deployId), HttpStatus.OK);
    }

    @ApiOperation("根据任务ID查询流程变量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping("/process-variables")
    public ResponseEntity<Map<String, Object>> processVariables(@RequestParam("taskId") String taskId) {
        return new ResponseEntity<>(flowTaskService.processVariables(taskId), HttpStatus.OK);
    }

    @ApiOperation("审批任务")
    @PostMapping("/complete")
    @OperationLog("审批任务")
    public ResponseEntity<Boolean> complete(@RequestBody FlowTaskQueryDto flowTaskQueryDto) {
        return new ResponseEntity<>(flowTaskService.complete(flowTaskQueryDto), HttpStatus.OK);
    }

    @ApiOperation("驳回任务")
    @PostMapping("/reject")
    @OperationLog("驳回任务")
    public ResponseEntity<?> rejectTask(@RequestBody FlowTaskQueryDto flowTaskQueryDto) {
        flowTaskService.rejectTask(flowTaskQueryDto);
        return new ResponseEntity<>("驳回任务成功", HttpStatus.OK);
    }

    @ApiOperation("退回任务")
    @PostMapping("/return")
    @OperationLog("退回任务")
    public ResponseEntity<?> returnTask(@RequestBody FlowTaskQueryDto flowTaskQueryDto) {
        flowTaskService.returnTask(flowTaskQueryDto);
        return new ResponseEntity<>("退回任务成功", HttpStatus.OK);
    }

    @ApiOperation("获取所有可回退的节点")
    @PostMapping("/return-task-list")
    public ResponseEntity<List<UserTask>> processVariables(@RequestBody FlowTaskQueryDto flowTaskQueryDto) {
        return new ResponseEntity<>(flowTaskService.findReturnTaskList(flowTaskQueryDto), HttpStatus.OK);
    }

    @ApiOperation("删除任务")
    @DeleteMapping("/delete-task")
    @OperationLog("删除任务")
    public ResponseEntity<?> deleteTask(@RequestBody FlowTaskQueryDto flowTaskQueryDto) {
        flowTaskService.deleteTask(flowTaskQueryDto);
        return new ResponseEntity<>("删除任务成功", HttpStatus.OK);
    }

    @ApiOperation("拾取任务")
    @PostMapping("/claim")
    @OperationLog("拾取任务")
    public ResponseEntity<?> claim(@RequestBody FlowTaskQueryDto flowTaskQueryDto) {
        flowTaskService.claim(flowTaskQueryDto);
        return new ResponseEntity<>("拾取任务成功", HttpStatus.OK);
    }

    @ApiOperation("取消拾取任务")
    @PostMapping("/un-claim")
    @OperationLog("取消拾取任务")
    public ResponseEntity<?> unClaim(@RequestBody FlowTaskQueryDto flowTaskQueryDto) {
        flowTaskService.unClaim(flowTaskQueryDto);
        return new ResponseEntity<>("取消拾取任务成功", HttpStatus.OK);
    }

    @ApiOperation("委派任务")
    @PostMapping("/delegate-task")
    @OperationLog("委派任务")
    public ResponseEntity<?> delegateTask(@RequestBody FlowTaskQueryDto flowTaskQueryDto) {
        flowTaskService.delegateTask(flowTaskQueryDto);
        return new ResponseEntity<>("委派任务成功", HttpStatus.OK);
    }

    @ApiOperation("归还任务")
    @PostMapping("/resolve-task")
    @OperationLog("归还任务")
    public ResponseEntity<?> resolveTask(@RequestBody FlowTaskQueryDto flowTaskQueryDto) {
        flowTaskService.resolveTask(flowTaskQueryDto);
        return new ResponseEntity<>("归还任务成功", HttpStatus.OK);
    }

    @ApiOperation("指派（转办）任务")
    @PostMapping("/assign-task")
    @OperationLog("指派（转办）任务")
    public ResponseEntity<?> assignTask(@RequestBody FlowTaskQueryDto flowTaskQueryDto) {
        flowTaskService.assignTask(flowTaskQueryDto);
        return new ResponseEntity<>("指派（转办）任务成功", HttpStatus.OK);
    }

    @ApiOperation("多实例加签")
    @PostMapping("/add-multi-instance-execution")
    @OperationLog("多实例加签")
    public ResponseEntity<?> multiInstanceExecution(@RequestBody FlowTaskQueryDto flowTaskQueryDto) {
        flowTaskService.addMultiInstanceExecution(flowTaskQueryDto);
        return new ResponseEntity<>("多实例加签成功", HttpStatus.OK);
    }

    @ApiOperation("多实例减签")
    @DeleteMapping("/delete-multi-instance-execution")
    @OperationLog("多实例减签")
    public ResponseEntity<?> deleteMultiInstanceExecution(@RequestBody FlowTaskQueryDto flowTaskQueryDto) {
        flowTaskService.deleteMultiInstanceExecution(flowTaskQueryDto);
        return new ResponseEntity<>("多实例减签成功", HttpStatus.OK);
    }

    @ApiOperation("获取下一节点")
    @PostMapping("/next-flow-node")
    public ResponseEntity<FlowNextNodeDto> nextFlowNode(@RequestBody FlowTaskQueryDto flowTaskQueryDto) {
        return new ResponseEntity<>(flowTaskService.getNextFlowNode(flowTaskQueryDto), HttpStatus.OK);
    }

    @ApiOperation("发起流程时获取下一节点")
    @PostMapping("/next-flow-node-by-start")
    public ResponseEntity<FlowNextNodeDto> nextFlowNodeByStart(@RequestBody FlowTaskQueryDto flowTaskQueryDto) {
        return new ResponseEntity<>(flowTaskService.getNextFlowNodeByStart(flowTaskQueryDto), HttpStatus.OK);
    }

    @ApiOperation("根据流程实例ID获取流程图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "procInsId", value = "流程实例ID", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping("/next-flow-node-by-start")
    public void nextFlowNodeByStart(@RequestParam("procInsId") String procInsId, HttpServerResponse resp) {
        InputStream is = flowTaskService.diagram(procInsId);
        if (is != null) {
            OutputStream os = null;
            BufferedImage bi;
            try {
                bi = ImageIO.read(is);
                resp.setContentType("image/png");
                os = resp.getOut();
                ImageIO.write(bi, "png", os);
            } catch (Exception e) {
                log.error("根据流程实例ID获取流程图异常：{}", e.getMessage());
                throw new BadRequestException("根据流程实例ID获取流程图异常，请稍后重试！");
            } finally {
                try {
                    if (os != null) {
                        os.flush();
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation("获取流程执行节点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "procInsId", value = "流程实例ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deployId", value = "流程部署ID", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping("/flow-viewer")
    public ResponseEntity<List<FlowViewerDto>> flowViewer(@RequestParam("procInsId") String procInsId, @RequestParam("deployId") String deployId) {
        return new ResponseEntity<>(flowTaskService.getFlowViewer(procInsId, deployId), HttpStatus.OK);
    }

    @ApiOperation("流程节点信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "procInsId", value = "流程实例ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deployId", value = "流程部署ID", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping("/flow-xml-node")
    public ResponseEntity<Map<String, Object>> flowXmlNode(@RequestParam("procInsId") String procInsId, @RequestParam("deployId") String deployId) throws IOException {
        return new ResponseEntity<>(flowTaskService.flowXmlAndNode(procInsId, deployId), HttpStatus.OK);
    }

    @ApiOperation("流程节点表单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping("/flow-task-form")
    public ResponseEntity<Map<String, Object>> flowTaskForm(@RequestParam("taskId") String taskId) {
        return new ResponseEntity<>(flowTaskService.flowTaskForm(taskId), HttpStatus.OK);
    }

    @ApiOperation("流程节点信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "procInsId", value = "流程实例ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "elementId", value = "元素ID", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping("/flow-task-info")
    public ResponseEntity<FlowTaskDto> flowTaskInfo(@RequestParam("procInsId") String procInsId, @RequestParam("elementId") String elementId) {
        return new ResponseEntity<>(flowTaskService.flowTaskInfo(procInsId, elementId), HttpStatus.OK);
    }

}
