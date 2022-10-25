package com.software.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.dto.ResponseResult;
import com.software.system.entity.Role;
import com.software.system.service.RoleService;
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
@RequestMapping("/role")
@Api(tags = "角色接口")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation("新增")
    @PostMapping
    @OperationLog("新增角色")
    public ResponseResult<Role> add(@Validated @RequestBody Role role) {
        if (roleService.add(role)) {
            return new ResponseResult<>(HttpStatus.OK.value(), "新增成功！", role);
        }
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "新增失败！", role);
    }

    @ApiOperation("修改")
    @PutMapping
    @OperationLog("修改角色")
    public ResponseResult<Role> update(@Validated @RequestBody Role role) {
        if (roleService.update(role)) {
            return new ResponseResult<>(HttpStatus.OK.value(), "修改成功！", role);
        }
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "修改失败！", role);
    }

    @ApiOperation("删除")
    @DeleteMapping
    @OperationLog("删除角色")
    public ResponseResult<String> delete(@RequestBody String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new IllegalArgumentException("id不能为空");
        }
        boolean result = roleService.delete((Long[]) ConvertUtils.convert(ids.split(StringConstant.COMMA), Long.class));
        return new ResponseResult<>(HttpStatus.OK.value(), result ? "删除成功！" : "删除失败！", ids);
    }

    @ApiOperation("根据id查询角色信息")
    @GetMapping("/id/{id}")
    public ResponseResult<Role> queryById(@NotNull @PathVariable("id") Long id) {
        return new ResponseResult<>(HttpStatus.OK.value(), roleService.queryById(id));
    }

    @ApiOperation("根据名称查询角色信息")
    @GetMapping("/name/{name}")
    public ResponseResult<Role> queryByName(@NotNull @PathVariable("name") String name) {
        return new ResponseResult<>(HttpStatus.OK.value(), roleService.queryByName(name));
    }

    @ApiOperation("查询角色列表")
    @GetMapping("queryList")
    public ResponseResult<List<Role>> queryList() {
        return new ResponseResult<>(HttpStatus.OK.value(), roleService.queryList());
    }

    @ApiOperation("分页查询角色信息")
    @GetMapping("/queryPage")
    public ResponseResult<Page<Role>> queryPage(Role role, QueryRequest queryRequest) {
        return new ResponseResult<>(HttpStatus.OK.value(), roleService.queryPage(role, queryRequest));
    }
}
