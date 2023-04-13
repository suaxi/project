package com.software.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.dto.QueryRequest;
import com.software.exception.BadRequestException;
import com.software.system.entity.Menu;
import com.software.system.entity.Role;
import com.software.system.service.RoleService;
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
import java.util.stream.Collectors;

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
    @PreAuthorize("@pre.check('role:add')")
    public ResponseEntity<Role> add(@Validated @RequestBody Role role) {
        roleService.add(role);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @ApiOperation("修改")
    @PutMapping
    @OperationLog("修改角色")
    @PreAuthorize("@pre.check('role:edit')")
    public ResponseEntity<Role> update(@Validated @RequestBody Role role) {
        roleService.update(role);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @ApiOperation("删除")
    @DeleteMapping
    @OperationLog("删除角色")
    @PreAuthorize("@pre.check('role:del')")
    public ResponseEntity<String> delete(@RequestBody List<Long> ids) {
        if (ids.size() == 0) {
            throw new IllegalArgumentException("id不能为空");
        }
        roleService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("根据id查询角色信息")
    @GetMapping("/id/{id}")
    @PreAuthorize("@pre.check('role:list')")
    public ResponseEntity<Role> queryById(@NotNull @PathVariable("id") Long id) {
        return new ResponseEntity<>(roleService.queryById(id), HttpStatus.OK);
    }

    @ApiOperation("根据名称查询角色信息")
    @GetMapping("/name/{name}")
    @PreAuthorize("@pre.check('role:list')")
    public ResponseEntity<Role> queryByName(@NotNull @PathVariable("name") String name) {
        return new ResponseEntity<>(roleService.queryByName(name), HttpStatus.OK);
    }

    @ApiOperation("查询角色列表")
    @GetMapping("queryList")
    @PreAuthorize("@pre.check('role:list')")
    public ResponseEntity<List<Role>> queryList() {
        return new ResponseEntity<>(roleService.queryList(), HttpStatus.OK);
    }

    @ApiOperation("分页查询角色信息")
    @GetMapping("/queryPage")
    @PreAuthorize("@pre.check('role:list')")
    public ResponseEntity<Page<Role>> queryPage(Role role, QueryRequest queryRequest) {
        return new ResponseEntity<>(roleService.queryPage(role, queryRequest), HttpStatus.OK);
    }

    @ApiOperation("修改角色菜单关联数据")
    @PutMapping("/menu")
    @OperationLog("修改角色菜单关联数据")
    @PreAuthorize("@pre.check('role:list')")
    public ResponseEntity<?> updateRoleMenu(@RequestBody Role role) {
        List<Menu> menus = role.getMenus();
        if (menus == null) {
            throw new BadRequestException("菜单id不能为空");
        }
        roleService.updateRoleMenu(role.getId(), menus.stream().map(Menu::getId).collect(Collectors.toList()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
