package com.software.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.dto.ResponseResult;
import com.software.system.entity.Job;
import com.software.system.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseResult<Job> add(@Validated @RequestBody Job job) {
        if (jobService.add(job)) {
            return new ResponseResult<>(HttpStatus.OK.value(), "新增成功！", job);
        }
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "新增失败！", job);
    }

    @ApiOperation("修改")
    @PutMapping
    @OperationLog("修改岗位")
    public ResponseResult<Job> update(@Validated @RequestBody Job job) {
        if (jobService.update(job)) {
            return new ResponseResult<>(HttpStatus.OK.value(), "修改成功！", job);
        }
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "修改失败！", job);
    }

    @ApiOperation("删除")
    @DeleteMapping
    @OperationLog("删除岗位")
    public ResponseResult<String> delete(@RequestBody String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new IllegalArgumentException("id不能为空");
        }
        boolean result = jobService.delete((Long[]) ConvertUtils.convert(ids.split(StringConstant.COMMA), Long.class));
        return new ResponseResult<>(HttpStatus.OK.value(), result ? "删除成功！" : "删除失败！", ids);
    }

    @ApiOperation("根据id查询岗位信息")
    @GetMapping("/id/{id}")
    public ResponseResult<Job> queryById(@NotNull @PathVariable("id") Long id) {
        return new ResponseResult<>(HttpStatus.OK.value(), jobService.queryById(id));
    }

    @ApiOperation("根据名称查询岗位信息")
    @GetMapping("/name/{name}")
    public ResponseResult<Job> queryByName(@NotNull @PathVariable("name") String name) {
        return new ResponseResult<>(HttpStatus.OK.value(), jobService.queryByName(name));
    }

    @ApiOperation("查询角色列表")
    @GetMapping("queryList")
    public ResponseResult<List<Job>> queryList() {
        return new ResponseResult<>(HttpStatus.OK.value(), jobService.queryList());
    }

    @ApiOperation("分页查询岗位信息")
    @GetMapping("/queryPage")
    public ResponseResult<Page<Job>> queryPage(Job job, QueryRequest queryRequest) {
        return new ResponseResult<>(HttpStatus.OK.value(), jobService.queryPage(job, queryRequest));
    }
}
