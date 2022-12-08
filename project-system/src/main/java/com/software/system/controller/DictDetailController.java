package com.software.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.dto.QueryRequest;
import com.software.system.entity.DictDetail;
import com.software.system.service.DictDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:45
 */
@RestController
@RequestMapping("/dictDetail")
@Api(tags = "数据字典详情接口")
public class DictDetailController {

    @Autowired
    private DictDetailService dictDetailService;

    @ApiOperation("新增")
    @PostMapping
    @OperationLog("新增数据字典详情")
    public ResponseEntity<DictDetail> add(@Validated @RequestBody DictDetail dictDetail) {
        dictDetailService.add(dictDetail);
        return new ResponseEntity<>(dictDetail, HttpStatus.OK);
    }

    @ApiOperation("修改")
    @PutMapping
    @OperationLog("修改数据字典详情")
    public ResponseEntity<DictDetail> update(@Validated @RequestBody DictDetail dictDetail) {
        dictDetailService.update(dictDetail);
        return new ResponseEntity<>(dictDetail, HttpStatus.OK);
    }

    @ApiOperation("删除")
    @DeleteMapping
    @OperationLog("删除数据字典详情")
    public ResponseEntity<String> delete(@RequestBody List<Long> ids) {
        if (ids.size() == 0) {
            throw new IllegalArgumentException("id不能为空");
        }
        dictDetailService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("根据id查询数据字典详情信息")
    @GetMapping("/id/{id}")
    public ResponseEntity<DictDetail> queryById(@NotNull @PathVariable("id") Long id) {
        return new ResponseEntity<>(dictDetailService.queryById(id), HttpStatus.OK);
    }

    @ApiOperation("根据字典id查询对应数据字典详情信息")
    @GetMapping("/dictId/{dictId}")
    public ResponseEntity<List<DictDetail>> queryByName(@NotNull @PathVariable("dictId") Long dictId) {
        return new ResponseEntity<>(dictDetailService.queryByDictId(dictId), HttpStatus.OK);
    }

    @ApiOperation("查询角色列表")
    @GetMapping("queryList")
    public ResponseEntity<List<DictDetail>> queryList() {
        return new ResponseEntity<>(dictDetailService.queryList(), HttpStatus.OK);
    }

    @ApiOperation("分页查询数据字典详情信息")
    @GetMapping("/queryPage")
    public ResponseEntity<Page<DictDetail>> queryPage(DictDetail dictDetail, QueryRequest queryRequest) {
        return new ResponseEntity<>(dictDetailService.queryPage(dictDetail, queryRequest), HttpStatus.OK);
    }
}
