package com.software.workflow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.dto.QueryRequest;
import com.software.workflow.entity.FlowListener;
import com.software.workflow.service.FlowListenerService;
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
 * @date 2024/12/21 17:52
 */
@RestController
@RequestMapping("/workflow/listener")
@Api(tags = "流程监听接口")
public class FlowListenerController {
    
    @Autowired
    private FlowListenerService flowListenerService;

    @ApiOperation("新增")
    @PostMapping
    @OperationLog("新增流程监听")
    public ResponseEntity<FlowListener> add(@Validated @RequestBody FlowListener FlowListener) {
        flowListenerService.add(FlowListener);
        return new ResponseEntity<>(FlowListener, HttpStatus.OK);
    }

    @ApiOperation("修改")
    @PutMapping
    @OperationLog("修改流程监听")
    public ResponseEntity<FlowListener> update(@Validated @RequestBody FlowListener FlowListener) {
        flowListenerService.update(FlowListener);
        return new ResponseEntity<>(FlowListener, HttpStatus.OK);
    }

    @ApiOperation("删除")
    @DeleteMapping
    @OperationLog("删除流程监听")
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        if (ids.size() == 0) {
            throw new IllegalArgumentException("id不能为空");
        }
        flowListenerService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("根据ID查询流程监听")
    @GetMapping("/id/{id}")
    public ResponseEntity<FlowListener> id(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(flowListenerService.findById(id), HttpStatus.OK);
    }

    @ApiOperation("查询流程监听列表")
    @PostMapping("/list")
    public ResponseEntity<List<FlowListener>> list(@RequestBody FlowListener FlowListener) {
        return new ResponseEntity<>(flowListenerService.queryList(FlowListener), HttpStatus.OK);
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public ResponseEntity<Page<FlowListener>> page(FlowListener FlowListener, QueryRequest queryRequest) {
        return new ResponseEntity<>(flowListenerService.queryPage(FlowListener, queryRequest), HttpStatus.OK);
    }

}
