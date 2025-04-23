package com.software.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.dto.QueryRequest;
import com.software.system.entity.User;
import com.software.system.service.UserService;
import com.software.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

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
    @PreAuthorize("@pre.check('user:add')")
    public ResponseEntity<User> add(@Validated @RequestBody User user) {
        userService.add(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation("修改")
    @PutMapping
    @OperationLog("修改用户")
    @PreAuthorize("@pre.check('user:edit')")
    public ResponseEntity<User> update(@Validated @RequestBody User user) {
        userService.update(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation("删除")
    @DeleteMapping
    @OperationLog("删除用户")
    @PreAuthorize("@pre.check('user:del')")
    public ResponseEntity<Integer[]> delete(@RequestBody Integer[] ids) {
        if (ids.length == 0) {
            throw new IllegalArgumentException("id不能为空");
        }
        userService.delete(ids);
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @ApiOperation("根据id查询用户信息")
    @GetMapping("/id/{id}")
    @PreAuthorize("@pre.check('user:list')")
    public ResponseEntity<User> id(@NotNull @PathVariable("id") Integer id) {
        return new ResponseEntity<>(userService.queryById(id), HttpStatus.OK);
    }

    @ApiOperation("根据用户名查询用户信息")
    @GetMapping("/name/{name}")
    @PreAuthorize("@pre.check('user:list')")
    public ResponseEntity<User> name(@NotNull @PathVariable("name") String name) {
        return new ResponseEntity<>(userService.queryByName(name), HttpStatus.OK);
    }

    @ApiOperation("分页查询用户信息")
    @GetMapping("/page")
    @PreAuthorize("@pre.check('user:list')")
    public ResponseEntity<Page<User>> page(User user, QueryRequest queryRequest) {
        return new ResponseEntity<>(userService.queryPage(user, queryRequest), HttpStatus.OK);
    }

    @ApiOperation("查询用户列表")
    @PostMapping("/list")
    public ResponseEntity<List<User>> list(@RequestBody User user) {
        return new ResponseEntity<>(userService.queryList(user), HttpStatus.OK);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/user-info")
    @PreAuthorize("@pre.check('user:list')")
    public ResponseEntity<UserDetails> userInfo() {
        return new ResponseEntity<>(SecurityUtils.getCurrentUser(), HttpStatus.OK);
    }

}
