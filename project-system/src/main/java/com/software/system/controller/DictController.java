package com.software.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.dto.QueryRequest;
import com.software.system.entity.Dict;
import com.software.system.service.DictService;
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
 * @date 2022/12/08 21:45
 */
@RestController
@RequestMapping("/dict")
@Api(tags = "数据字典接口")
public class DictController {

    @Autowired
    private DictService dictService;

    @ApiOperation("新增")
    @PostMapping
    @OperationLog("新增数据字典")
    public ResponseEntity<Dict> add(@Validated @RequestBody Dict dict) {
        dictService.add(dict);
        return new ResponseEntity<>(dict, HttpStatus.OK);
    }

    @ApiOperation("修改")
    @PutMapping
    @OperationLog("修改数据字典")
    public ResponseEntity<Dict> update(@Validated @RequestBody Dict dict) {
        dictService.update(dict);
        return new ResponseEntity<>(dict, HttpStatus.OK);
    }

    @ApiOperation("删除")
    @DeleteMapping
    @OperationLog("删除数据字典")
    public ResponseEntity<String> delete(@RequestBody List<Long> ids) {
        if (ids.size() == 0) {
            throw new IllegalArgumentException("id不能为空");
        }
        dictService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("根据id查询数据字典信息")
    @GetMapping("/id/{id}")
    public ResponseEntity<Dict> queryById(@NotNull @PathVariable("id") Long id) {
        return new ResponseEntity<>(dictService.queryById(id), HttpStatus.OK);
    }

    @ApiOperation("根据名称查询数据字典信息")
    @GetMapping("/name/{name}")
    public ResponseEntity<Dict> queryByName(@NotNull @PathVariable("name") String name) {
        return new ResponseEntity<>(dictService.queryByName(name), HttpStatus.OK);
    }

    @ApiOperation("查询数据字典列表")
    @GetMapping("queryList")
    public ResponseEntity<List<Dict>> queryList() {
        return new ResponseEntity<>(dictService.queryList(), HttpStatus.OK);
    }

    @ApiOperation("分页查询数据字典信息")
    @GetMapping("/queryPage")
    public ResponseEntity<Page<Dict>> queryPage(Dict dict, QueryRequest queryRequest) {
        return new ResponseEntity<>(dictService.queryPage(dict, queryRequest), HttpStatus.OK);
    }
}
