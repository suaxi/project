package com.software.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.dto.QueryRequest;
import com.software.entity.VueRouter;
import com.software.system.dto.MenuDto;
import com.software.system.entity.Menu;
import com.software.system.service.MenuService;
import com.software.utils.ProjectUtils;
import com.software.utils.SecurityUtils;
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
import java.util.Map;
import java.util.Set;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:45
 */
@RestController
@RequestMapping("/menu")
@Api(tags = "菜单接口")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation("新增")
    @PostMapping
    @OperationLog("新增菜单")
    @PreAuthorize("@pre.check('menu:add')")
    public ResponseEntity<Menu> add(@Validated @RequestBody Menu menu) {
        menuService.add(menu);
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @ApiOperation("修改")
    @PutMapping
    @OperationLog("修改菜单")
    @PreAuthorize("@pre.check('menu:edit')")
    public ResponseEntity<Menu> update(@Validated @RequestBody Menu menu) {
        menuService.update(menu);
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @ApiOperation("删除")
    @DeleteMapping
    @OperationLog("删除菜单")
    @PreAuthorize("@pre.check('menu:del')")
    public ResponseEntity<String> delete(@RequestBody List<Integer> ids) {
        if (ids.size() == 0) {
            throw new IllegalArgumentException("id不能为空");
        }
        menuService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("根据id查询菜单信息")
    @GetMapping("/id/{id}")
    @PreAuthorize("@pre.check('menu:list')")
    public ResponseEntity<Menu> queryById(@NotNull @PathVariable("id") Integer id) {
        return new ResponseEntity<>(menuService.queryById(id), HttpStatus.OK);
    }

    @ApiOperation("根据名称查询菜单信息")
    @GetMapping("/name/{name}")
    @PreAuthorize("@pre.check('menu:list')")
    public ResponseEntity<Menu> queryByName(@NotNull @PathVariable("name") String name) {
        return new ResponseEntity<>(menuService.queryByName(name), HttpStatus.OK);
    }

    @ApiOperation("分页查询菜单信息")
    @GetMapping("/queryPage")
    @PreAuthorize("@pre.check('menu:list')")
    public ResponseEntity<Page<MenuDto>> queryPage(Menu menu, QueryRequest queryRequest) {
        return new ResponseEntity<>(menuService.queryPage(menu, queryRequest), HttpStatus.OK);
    }

    @ApiOperation("获取用户路由")
    @GetMapping("/getUserRouters")
    @PreAuthorize("@pre.check('menu:list')")
    public ResponseEntity<List<VueRouter<Menu>>> getUserRouters() {
        return new ResponseEntity<>(menuService.getUserRouters(SecurityUtils.getCurrentUserId()), HttpStatus.OK);
    }

    @ApiOperation("根据父id查询全部菜单（懒加载）")
    @GetMapping("/queryChildListByPid")
    @PreAuthorize("@pre.check('menu:list')")
    public ResponseEntity<Map<String, Object>> queryChildListByPid(@RequestParam("pid") Integer pid) {
        List<MenuDto> menuDtoList = menuService.queryChildListByPid(pid);
        return new ResponseEntity<>(ProjectUtils.toPageData(menuDtoList, menuDtoList.size()), HttpStatus.OK);
    }

    @ApiOperation("根据id查询子级菜单（包括自身）")
    @GetMapping("/queryMenuListById")
    @PreAuthorize("@pre.check('menu:list')")
    public ResponseEntity<Map<String, Object>> queryMenuListById(@RequestParam("id") Integer id) {
        Set<Menu> menuSet = menuService.queryMenuListById(id);
        return new ResponseEntity<>(ProjectUtils.toPageData(menuSet, menuSet.size()), HttpStatus.OK);
    }

    @ApiOperation("根据id查询同级与上级菜单列表")
    @GetMapping("/querySameLevelAndSuperiorMenuListById")
    @PreAuthorize("@pre.check('menu:list')")
    public ResponseEntity<Map<String, Object>> querySameLevelAndSuperiorMenuListById(@RequestParam("id") Integer id) {
        List<MenuDto> menuDtoList = menuService.querySameLevelAndSuperiorMenuListById(id);
        return new ResponseEntity<>(ProjectUtils.toPageData(menuDtoList, menuDtoList.size()), HttpStatus.OK);
    }
}
