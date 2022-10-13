package com.software.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.dto.ResponseResult;
import com.software.entity.Log;
import com.software.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wang Hao
 * @date 2022/10/9 23:02
 */
@RestController
@RequestMapping("/log")
@Api(tags = "系统日志接口")
public class LogController {

    @Autowired
    private LogService logService;

    @PostMapping
    @ApiOperation("新增")
    public ResponseResult<Log> add(Log log) {
        logService.add(log);
        return new ResponseResult<>(HttpStatus.OK.value(), log);
    }

    @DeleteMapping
    @ApiOperation("删除")
    public ResponseResult<String> delete(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new IllegalArgumentException("id不能为空");
        }
        boolean result = logService.delete((Long[]) ConvertUtils.convert(ids.split(StringConstant.COMMA), Long.class));
        return new ResponseResult<>(HttpStatus.OK.value(), result ? "删除成功！" : "删除失败！", ids);
    }

    @GetMapping("/queryPage")
    @ApiOperation("分页查询日志信息")
    public ResponseResult<Page<Log>> queryPage(Log log, QueryRequest queryRequest) {
        return new ResponseResult<>(HttpStatus.OK.value(), logService.queryPage(log, queryRequest));
    }
}
