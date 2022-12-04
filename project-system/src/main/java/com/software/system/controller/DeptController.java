package com.software.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.dto.QueryRequest;
import com.software.dto.Tree;
import com.software.system.dto.DeptDto;
import com.software.system.dto.DeptDtoTree;
import com.software.system.entity.Dept;
import com.software.system.service.DeptService;
import com.software.utils.ProjectUtils;
import com.software.utils.TreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:45
 */
@RestController
@RequestMapping("/dept")
@Api(tags = "部门接口")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @ApiOperation("新增")
    @PostMapping
    @OperationLog("新增部门")
    public ResponseEntity<Dept> add(@Validated @RequestBody Dept dept) {
        deptService.add(dept);
        return new ResponseEntity<>(dept, HttpStatus.OK);
    }

    @ApiOperation("修改")
    @PutMapping
    @OperationLog("修改部门")
    public ResponseEntity<Dept> update(@Validated @RequestBody Dept dept) {
        deptService.update(dept);
        return new ResponseEntity<>(dept, HttpStatus.OK);
    }

    @ApiOperation("删除")
    @DeleteMapping
    @OperationLog("删除部门")
    public ResponseEntity<String> delete(@RequestBody List<Long> ids) {
        if (ids.size() == 0) {
            throw new IllegalArgumentException("id不能为空");
        }
        deptService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("根据id查询部门信息")
    @GetMapping("/id/{id}")
    public ResponseEntity<Dept> queryById(@NotNull @PathVariable("id") Long id) {
        return new ResponseEntity<>(deptService.queryById(id), HttpStatus.OK);
    }

    @ApiOperation("根据名称查询部门信息")
    @GetMapping("/name/{name}")
    public ResponseEntity<Dept> queryByName(@NotNull @PathVariable("name") String name) {
        return new ResponseEntity<>(deptService.queryByName(name), HttpStatus.OK);
    }

    @ApiOperation("分页查询部门信息")
    @GetMapping("/queryPage")
    public ResponseEntity<Page<DeptDto>> queryPage(Dept dept, QueryRequest queryRequest) {
        return new ResponseEntity<>(deptService.queryPage(dept, queryRequest), HttpStatus.OK);
    }

    @ApiOperation("根据父id查询子级部门")
    @GetMapping("/queryChildListByPid")
    public ResponseEntity<Map<String, Object>> queryChildListByPid(Long pid) {
        List<DeptDto> deptDtoList = deptService.queryChildListByPid(pid);
        return new ResponseEntity<>(ProjectUtils.toPageData(deptDtoList, deptDtoList.size()), HttpStatus.OK);
    }

    @ApiOperation("查询部门树")
    @GetMapping("/queryDeptTree")
    public ResponseEntity<List<? extends Tree<?>>> queryDeptTree() {
        return new ResponseEntity<>(deptService.queryDeptTree(), HttpStatus.OK);
    }

    @ApiOperation("根据id查找同级与上级部门树")
    @PostMapping("/querySuperiorListByIds")
    public ResponseEntity<Map<String, Object>> querySuperiorListByIds(@RequestBody List<Long> ids) {
        List<Dept> deptList = new ArrayList<>();
        if (ids != null && ids.size() > 0) {
            if (ids.contains(0L)) {
                throw new IllegalArgumentException("查询参数不合法，部门id不能为0");
            }
            for (Long id : ids) {
                Dept dept = deptService.queryById(id);
                deptService.querySuperiorList(dept, deptList);
            }
            List<DeptDtoTree> trees = new ArrayList<>();
            deptList.forEach(item -> {
                DeptDtoTree tree = new DeptDtoTree();
                tree.setId(item.getId());
                tree.setParentId(item.getPid() == null ? null : item.getPid());
                tree.setLabel(item.getName());
                tree.setSubCount(item.getSubCount());
                tree.setName(item.getName());
                tree.setSort(item.getSort());
                trees.add(tree);
            });
            List<? extends Tree<?>> treeResult = TreeUtil.build(trees);
            return new ResponseEntity<>(ProjectUtils.toPageData(treeResult, treeResult.size()), HttpStatus.OK);
        }
        return new ResponseEntity<>(ProjectUtils.toPageData(null, 0), HttpStatus.OK);
    }
}
