package com.software.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.dto.ResponseResult;
import com.software.entity.Dept;
import com.software.service.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:45
 */
@RestController
@RequestMapping("/dept")
@Api(tags = "部门接口")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @OperationLog("新增部门")
    @PostMapping
    @ApiOperation("新增")
    public ResponseResult<Dept> add(@Validated @RequestBody Dept dept) {
        if (deptService.add(dept)) {
            return new ResponseResult<>(HttpStatus.OK.value(), "新增成功！", dept);
        }
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "新增失败！", dept);
    }

    @OperationLog("修改部门")
    @PutMapping
    @ApiOperation("修改")
    public ResponseResult<Dept> update(@Validated @RequestBody Dept dept) {
        if (deptService.update(dept)) {
            return new ResponseResult<>(HttpStatus.OK.value(), "修改成功！", dept);
        }
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "修改失败！", dept);
    }

    @OperationLog("删除部门")
    @DeleteMapping
    @ApiOperation("删除")
    public ResponseResult<String> delete(@RequestBody String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new IllegalArgumentException("id不能为空");
        }
        boolean result = deptService.delete((Long[]) ConvertUtils.convert(ids.split(StringConstant.COMMA), Long.class));
        return new ResponseResult<>(HttpStatus.OK.value(), result ? "删除成功！" : "删除失败！", ids);
    }

    @GetMapping("/deptInfo/id/{id}")
    @ApiOperation("根据id查询部门信息")
    public ResponseResult<Dept> queryById(@NotNull @PathVariable("id") Long id) {
        return new ResponseResult<>(HttpStatus.OK.value(), deptService.queryById(id));
    }

    @GetMapping("/deptInfo/name/{name}")
    @ApiOperation("根据名称查询部门信息")
    public ResponseResult<Dept> queryByName(@NotNull @PathVariable("name") String name) {
        return new ResponseResult<>(HttpStatus.OK.value(), deptService.queryByName(name));
    }

    @GetMapping("/queryPage")
    @ApiOperation("分页查询部门信息")
    public ResponseResult<Page<Dept>> queryPage(Dept dept, QueryRequest queryRequest) {
        return new ResponseResult<>(HttpStatus.OK.value(), deptService.queryPage(dept, queryRequest));
    }
}
