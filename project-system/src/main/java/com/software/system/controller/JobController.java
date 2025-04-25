package com.software.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.dto.QueryRequest;
import com.software.system.entity.Job;
import com.software.system.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:45
 */
@RestController
@RequestMapping("/job")
@Api(tags = "岗位接口")
public class JobController {

    @Autowired
    private JobService jobService;

    @ApiOperation("新增")
    @PostMapping
    @OperationLog("新增岗位")
    @PreAuthorize("@pre.check('job:add')")
    public ResponseEntity<Job> add(@Validated @RequestBody Job job) {
        jobService.add(job);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    @ApiOperation("修改")
    @PutMapping
    @OperationLog("修改岗位")
    @PreAuthorize("@pre.check('job:edit')")
    public ResponseEntity<Job> update(@Validated @RequestBody Job job) {
        jobService.update(job);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    @ApiOperation("删除")
    @DeleteMapping
    @OperationLog("删除岗位")
    @PreAuthorize("@pre.check('job:del')")
    public ResponseEntity<String> delete(@RequestBody List<Long> ids) {
        if (ids.size() == 0) {
            throw new IllegalArgumentException("id不能为空");
        }
        jobService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("根据id查询岗位信息")
    @GetMapping("/id/{id}")
    @PreAuthorize("@pre.check('user:list', 'job:list')")
    public ResponseEntity<Job> id(@NotNull @PathVariable("id") Long id) {
        return new ResponseEntity<>(jobService.queryById(id), HttpStatus.OK);
    }

    @ApiOperation("根据名称查询岗位信息")
    @GetMapping("/name/{name}")
    @PreAuthorize("@pre.check('user:list', 'job:list')")
    public ResponseEntity<Job> name(@NotNull @PathVariable("name") String name) {
        return new ResponseEntity<>(jobService.queryByName(name), HttpStatus.OK);
    }

    @ApiOperation("查询岗位列表")
    @GetMapping("/list")
    @PreAuthorize("@pre.check('user:list', 'job:list')")
    public ResponseEntity<List<Job>> list() {
        return new ResponseEntity<>(jobService.queryList(), HttpStatus.OK);
    }

    @ApiOperation("分页查询岗位信息")
    @GetMapping("/page")
    @PreAuthorize("@pre.check('user:list', 'job:list')")
    public ResponseEntity<Page<Job>> page(Job job, QueryRequest queryRequest) {
        return new ResponseEntity<>(jobService.queryPage(job, queryRequest), HttpStatus.OK);
    }
}
