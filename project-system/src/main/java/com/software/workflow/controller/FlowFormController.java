package com.software.workflow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.dto.QueryRequest;
import com.software.workflow.entity.FlowDeployInsForm;
import com.software.workflow.entity.FlowForm;
import com.software.workflow.service.FlowFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author suaxi
 * @date 2024/12/11 21:45
 */
@RestController
@RequestMapping("/workflow/form")
@Api(tags = "流程表单接口")
public class FlowFormController {

    @Autowired
    private FlowFormService flowFormService;

    @ApiOperation("新增")
    @PostMapping
    @OperationLog("新增流程表单")
    public ResponseEntity<FlowForm> add(@Validated @RequestBody FlowForm flowForm) {
        flowFormService.add(flowForm);
        return new ResponseEntity<>(flowForm, HttpStatus.OK);
    }

    @ApiOperation("修改")
    @PutMapping
    @OperationLog("修改流程表单")
    public ResponseEntity<FlowForm> update(@Validated @RequestBody FlowForm flowForm) {
        flowFormService.update(flowForm);
        return new ResponseEntity<>(flowForm, HttpStatus.OK);
    }

    @ApiOperation("删除")
    @DeleteMapping
    @OperationLog("删除流程表单")
    public ResponseEntity<String> delete(@RequestBody List<Integer> ids) {
        if (ids.size() == 0) {
            throw new IllegalArgumentException("id不能为空");
        }
        flowFormService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("根据id查询流程表单信息")
    @GetMapping("/form-id/{formId}")
    public ResponseEntity<FlowForm> formId(@NotNull @PathVariable("formId") Integer formId) {
        return new ResponseEntity<>(flowFormService.queryByFormId(formId), HttpStatus.OK);
    }

    @ApiOperation("查询流程表单列表")
    @PostMapping("/list")
    public ResponseEntity<List<FlowForm>> list(@RequestBody FlowForm flowForm) {
        return new ResponseEntity<>(flowFormService.queryList(flowForm), HttpStatus.OK);
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public ResponseEntity<Page<FlowForm>> page(FlowForm flowForm, QueryRequest queryRequest) {
        return new ResponseEntity<>(flowFormService.queryPage(flowForm, queryRequest), HttpStatus.OK);
    }

    @ApiOperation("挂载流程表单")
    @PostMapping("/mount-flow-form")
    public ResponseEntity<Boolean> mountFlowForm(@Validated @RequestBody FlowDeployInsForm flowDeployInsForm) {
        return new ResponseEntity<>(flowFormService.mountFlowForm(flowDeployInsForm), HttpStatus.OK);
    }

}
