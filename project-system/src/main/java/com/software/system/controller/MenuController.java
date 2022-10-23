package com.software.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.dto.ResponseResult;
import com.software.entity.VueRouter;
import com.software.system.entity.Menu;
import com.software.system.service.MenuService;
import com.software.utils.SecurityUtils;
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
@RequestMapping("/menu")
@Api(tags = "菜单接口")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation("新增")
    @PostMapping
    @OperationLog("新增菜单")
    public ResponseResult<Menu> add(@Validated @RequestBody Menu menu) {
        if (menuService.add(menu)) {
            return new ResponseResult<>(HttpStatus.OK.value(), "新增成功！", menu);
        }
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "新增失败！", menu);
    }

    @ApiOperation("修改")
    @PutMapping
    @OperationLog("修改菜单")
    public ResponseResult<Menu> update(@Validated @RequestBody Menu menu) {
        if (menuService.update(menu)) {
            return new ResponseResult<>(HttpStatus.OK.value(), "修改成功！", menu);
        }
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "修改失败！", menu);
    }

    @ApiOperation("删除")
    @DeleteMapping
    @OperationLog("删除菜单")
    public ResponseResult<String> delete(@RequestBody String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new IllegalArgumentException("id不能为空");
        }
        boolean result = menuService.delete((Long[]) ConvertUtils.convert(ids.split(StringConstant.COMMA), Long.class));
        return new ResponseResult<>(HttpStatus.OK.value(), result ? "删除成功！" : "删除失败！", ids);
    }

    @ApiOperation("根据id查询菜单信息")
    @GetMapping("/id/{id}")
    public ResponseResult<Menu> queryById(@NotNull @PathVariable("id") Long id) {
        return new ResponseResult<>(HttpStatus.OK.value(), menuService.queryById(id));
    }

    @ApiOperation("根据名称查询菜单信息")
    @GetMapping("/name/{name}")
    public ResponseResult<Menu> queryByName(@NotNull @PathVariable("name") String name) {
        return new ResponseResult<>(HttpStatus.OK.value(), menuService.queryByName(name));
    }

    @ApiOperation("分页查询菜单信息")
    @GetMapping("/queryPage")
    public ResponseResult<Page<Menu>> queryPage(Menu menu, QueryRequest queryRequest) {
        return new ResponseResult<>(HttpStatus.OK.value(), menuService.queryPage(menu, queryRequest));
    }

    @ApiOperation("获取用户路由")
    @GetMapping("/getUserRouters")
    public ResponseResult<List<VueRouter<Menu>>> getUserRouters() {
        return new ResponseResult<>(HttpStatus.OK.value(), menuService.getUserRouters(SecurityUtils.getCurrentUserId()));
    }
}
