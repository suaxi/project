package com.software.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wang Hao
 * @date 2022/10/8 22:49
 */
@RestController
@RequestMapping("/test")
@Api(tags = "测试接口")
public class IndexController {

    @ApiOperation("基础环境测试")
    @GetMapping
    public String test() {
        return "test";
    }
}
