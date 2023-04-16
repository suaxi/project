package com.software.system.controller;

import com.software.annotation.Limit;
import com.software.annotation.OperationLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @OperationLog("接口测试")
    @ApiOperation("基础环境测试")
    @GetMapping
    @Limit(name = "基础环境测试（接口限流）", key = "test", period = 60, count = 10)
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("test", HttpStatus.OK);
    }
}
