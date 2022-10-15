package com.software.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.dto.ResponseResult;
import com.software.system.entity.User;
import com.software.system.service.UserService;
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
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    @OperationLog("新增用户")
    @PostMapping
    @ApiOperation("新增")
    public ResponseResult<User> add(@Validated @RequestBody User user) {
        if (userService.add(user)) {
            return new ResponseResult<>(HttpStatus.OK.value(), "新增成功！", user);
        }
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "新增失败！", user);
    }

    @OperationLog("修改用户")
    @PutMapping
    @ApiOperation("修改")
    public ResponseResult<User> update(@Validated @RequestBody User user) {
        if (userService.update(user)) {
            return new ResponseResult<>(HttpStatus.OK.value(), "修改成功！", user);
        }
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "修改失败！", user);
    }

    @OperationLog("删除用户")
    @DeleteMapping
    @ApiOperation("删除")
    public ResponseResult<String> delete(@RequestBody String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new IllegalArgumentException("id不能为空");
        }
        boolean result = userService.delete((Long[]) ConvertUtils.convert(ids.split(StringConstant.COMMA), Long.class));
        return new ResponseResult<>(HttpStatus.OK.value(), result ? "删除成功！" : "删除失败！", ids);
    }

    @GetMapping("/userInfo/id/{id}")
    @ApiOperation("根据id查询用户信息")
    public ResponseResult<User> queryById(@NotNull @PathVariable("id") Long id) {
        return new ResponseResult<>(HttpStatus.OK.value(), userService.queryById(id));
    }

    @GetMapping("/userInfo/name/{name}")
    @ApiOperation("根据用户名查询用户信息")
    public ResponseResult<User> queryByName(@NotNull @PathVariable("name") String name) {
        return new ResponseResult<>(HttpStatus.OK.value(), userService.queryByName(name));
    }

    @GetMapping("/queryPage")
    @ApiOperation("分页查询用户信息")
    public ResponseResult<Page<User>> queryPage(User user, QueryRequest queryRequest) {
        return new ResponseResult<>(HttpStatus.OK.value(), userService.queryPage(user, queryRequest));
    }
}
