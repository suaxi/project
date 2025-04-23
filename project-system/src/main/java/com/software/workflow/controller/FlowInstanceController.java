package com.software.workflow.controller;

import com.software.annotation.OperationLog;
import com.software.exception.BadRequestException;
import com.software.workflow.service.FlowInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @author suaxi
 * @date 2024/12/12 16:48
 */
@RestController
@RequestMapping("/workflow/form")
@Api(tags = "流程实例接口")
public class FlowInstanceController {

    @Autowired
    private FlowInstanceService flowInstanceService;

    @ApiOperation(value = "发起流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "procDefId", value = "流程定义ID", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "variables", value = "流程变量", required = true, paramType = "body", dataType = "object")
    })
    @PostMapping("/start-process/{procDefId}")
    @OperationLog("发起流程")
    public ResponseEntity<Boolean> startProcess(@PathVariable(value = "procDefId") String procDefId, @RequestBody Map<String, Object> variables) {
        return new ResponseEntity<>(flowInstanceService.startProcessInstanceById(procDefId, variables), HttpStatus.OK);
    }

    @ApiOperation(value = "激活或挂起流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "state", value = "状态（1：激活，2：挂起）", required = true, paramType = "path", dataType = "Integer"),
            @ApiImplicitParam(name = "procInsId", value = "流程实例ID", required = true, paramType = "path", dataType = "String")
    })
    @PutMapping("/update-state/{state}/{procInsId}")
    @OperationLog("激活或挂起流程实例")
    public ResponseEntity<?> updateState(@PathVariable(value = "state") Integer state, @PathVariable(value = "procInsId") String procInsId) {
        if (!Arrays.asList(1, 2).contains(state)) {
            throw new BadRequestException("状态不正确！");
        }
        flowInstanceService.updateState(state, procInsId);
        return new ResponseEntity<>("操作成功！", HttpStatus.OK);
    }

    @ApiOperation(value = "删除流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "procInsId", value = "流程实例ID", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "deleteReason", value = "删除原因", paramType = "body", dataType = "String"),
    })
    @DeleteMapping("/delete/{procInsId}")
    @OperationLog("删除流程")
    public ResponseEntity<?> stopProcessInstance(@PathVariable(value = "procInsId") String procInsId, @RequestBody String deleteReason) {
        flowInstanceService.delete(procInsId, deleteReason);
        return new ResponseEntity<>("删除成功！", HttpStatus.OK);
    }
}
