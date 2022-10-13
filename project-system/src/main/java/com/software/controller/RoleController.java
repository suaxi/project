package com.software.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.dto.ResponseResult;
import com.software.entity.Role;
import com.software.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

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

    @OperationLog("新增角色")
    @PostMapping
    @ApiOperation("新增")
    public ResponseResult<Role> add(Role role) {
        if (roleService.add(role)) {
            return new ResponseResult<>(HttpStatus.OK.value(), "新增成功！", role);
        }
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "新增失败！", role);
    }

    @OperationLog("修改角色")
    @PutMapping
    @ApiOperation("修改")
    public ResponseResult<Role> update(Role role) {
        if (roleService.update(role)) {
            return new ResponseResult<>(HttpStatus.OK.value(), "修改成功！", role);
        }
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "修改失败！", role);
    }

    @OperationLog("删除角色")
    @DeleteMapping
    @ApiOperation("删除")
    public ResponseResult<String> delete(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new IllegalArgumentException("id不能为空");
        }
        boolean result = roleService.delete((Long[]) ConvertUtils.convert(ids.split(StringConstant.COMMA), Long.class));
        return new ResponseResult<>(HttpStatus.OK.value(), result ? "删除成功！" : "删除失败！", ids);
    }

    @GetMapping("/roleInfo/id/{id}")
    @ApiOperation("根据id查询角色信息")
    public ResponseResult<Role> queryById(@NotNull @PathVariable("id") Long id) {
        return new ResponseResult<>(HttpStatus.OK.value(), roleService.queryById(id));
    }

    @GetMapping("/roleInfo/name/{name}")
    @ApiOperation("根据名称查询角色信息")
    public ResponseResult<Role> queryByName(@NotNull @PathVariable("name") String name) {
        return new ResponseResult<>(HttpStatus.OK.value(), roleService.queryByName(name));
    }

    @GetMapping("/queryPage")
    @ApiOperation("分页查询角色信息")
    public ResponseResult<Page<Role>> queryPage(Role role, QueryRequest queryRequest) {
        return new ResponseResult<>(HttpStatus.OK.value(), roleService.queryPage(role, queryRequest));
    }
}
