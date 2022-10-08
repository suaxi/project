package com.software.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wang Hao
 * @date 2022/10/8 22:49
 */
@RestController
public class IndexController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
