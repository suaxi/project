package com.software.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.dto.QueryRequest;
import com.software.dto.ResponseResult;
import com.software.system.entity.User;
import com.software.system.service.UserService;
import com.software.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
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

    @ApiOperation("新增")
    @PostMapping
    @OperationLog("新增用户")
    public ResponseResult<User> add(@Validated @RequestBody User user) {
        if (userService.add(user)) {
            return new ResponseResult<>(HttpStatus.OK.value(), "新增成功！", user);
        }
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "新增失败！", user);
    }

    @ApiOperation("修改")
    @PutMapping
    @OperationLog("修改用户")
    public ResponseResult<User> update(@Validated @RequestBody User user) {
        if (userService.update(user)) {
            return new ResponseResult<>(HttpStatus.OK.value(), "修改成功！", user);
        }
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "修改失败！", user);
    }

    @ApiOperation("删除")
    @DeleteMapping
    @OperationLog("删除用户")
    public ResponseResult<Long[]> delete(@RequestBody Long[] ids) {
        if (ids.length == 0) {
            throw new IllegalArgumentException("id不能为空");
        }
        if (userService.delete(ids)) {
            return new ResponseResult<>(HttpStatus.OK.value(), "删除成功！", ids);
        }
        return new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "删除失败！", ids);
    }

    @ApiOperation("根据id查询用户信息")
    @GetMapping("/id/{id}")
    public ResponseResult<User> queryById(@NotNull @PathVariable("id") Long id) {
        return new ResponseResult<>(HttpStatus.OK.value(), userService.queryById(id));
    }

    @ApiOperation("根据用户名查询用户信息")
    @GetMapping("/name/{name}")
    public ResponseResult<User> queryByName(@NotNull @PathVariable("name") String name) {
        return new ResponseResult<>(HttpStatus.OK.value(), userService.queryByName(name));
    }

    @ApiOperation("分页查询用户信息")
    @GetMapping("/queryPage")
    public ResponseResult<Page<User>> queryPage(User user, QueryRequest queryRequest) {
        return new ResponseResult<>(HttpStatus.OK.value(), userService.queryPage(user, queryRequest));
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/getUserInfo")
    public ResponseResult<UserDetails> getUserInfo() {
        return new ResponseResult<>(HttpStatus.OK.value(), SecurityUtils.getCurrentUser());
    }

}
