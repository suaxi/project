package com.software.workflow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.dto.QueryRequest;
import com.software.workflow.entity.FlowExpression;
import com.software.workflow.service.FlowExpressionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author suaxi
 * @date 2024/12/19 13:48
 */
@RestController
@RequestMapping("/workflow/expression")
@Api(tags = "流程表达式接口")
public class FlowExpressionController {

    @Autowired
    private FlowExpressionService flowExpressionService;

    @ApiOperation("新增")
    @PostMapping
    @OperationLog("新增流程表达式")
    public ResponseEntity<FlowExpression> add(@Validated @RequestBody FlowExpression FlowExpression) {
        flowExpressionService.add(FlowExpression);
        return new ResponseEntity<>(FlowExpression, HttpStatus.OK);
    }

    @ApiOperation("修改")
    @PutMapping
    @OperationLog("修改流程表达式")
    public ResponseEntity<FlowExpression> update(@Validated @RequestBody FlowExpression FlowExpression) {
        flowExpressionService.update(FlowExpression);
        return new ResponseEntity<>(FlowExpression, HttpStatus.OK);
    }

    @ApiOperation("删除")
    @DeleteMapping
    @OperationLog("删除流程表达式")
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        if (ids.size() == 0) {
            throw new IllegalArgumentException("id不能为空");
        }
        flowExpressionService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("根据ID查询流程表达式")
    @GetMapping("/id/{id}")
    public ResponseEntity<FlowExpression> id(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(flowExpressionService.findById(id), HttpStatus.OK);
    }

    @ApiOperation("查询流程表达式列表")
    @PostMapping("/list")
    public ResponseEntity<List<FlowExpression>> list(@RequestBody FlowExpression FlowExpression) {
        return new ResponseEntity<>(flowExpressionService.queryList(FlowExpression), HttpStatus.OK);
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public ResponseEntity<Page<FlowExpression>> page(FlowExpression FlowExpression, QueryRequest queryRequest) {
        return new ResponseEntity<>(flowExpressionService.queryPage(FlowExpression, queryRequest), HttpStatus.OK);
    }

}
