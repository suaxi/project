package com.software.workflow.controller;

import com.software.workflow.service.FlowTaskService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author suaxi
 * @date 2024/12/11 22:09
 */
@RestController
@RequestMapping("/workflow/task")
@Api(tags = "流程任务接口")
public class FlowTaskController {

    @Autowired
    private FlowTaskService flowTaskService;

}
