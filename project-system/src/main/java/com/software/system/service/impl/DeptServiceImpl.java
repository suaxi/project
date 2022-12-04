package com.software.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.dto.QueryRequest;
import com.software.dto.Tree;
import com.software.system.dto.DeptDto;
import com.software.system.dto.DeptDtoTree;
import com.software.system.entity.Dept;
import com.software.system.mapper.DeptMapper;
import com.software.system.service.DeptService;
import com.software.utils.SecurityUtils;
import com.software.utils.TreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:38
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(Dept dept) {
        dept.setCreateBy(SecurityUtils.getCurrentUserId());
        boolean flag = this.save(dept);
        if (dept.getPid() != null) {
            Dept parentDept = this.getById(dept.getPid());
            parentDept.setSubCount(parentDept.getSubCount() + 1);
            this.updateById(parentDept);
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Dept dept) {
        //原pid
        Dept oldDeptData = this.getById(dept.getId());
        Long pid = oldDeptData.getPid() == null ? null : oldDeptData.getPid();
        dept.setUpdateBy(SecurityUtils.getCurrentUserId());
        boolean flag = this.updateById(dept);
        //原父节点子部门数量处理
        if (pid != null && flag) {
            this.dealParentDeptSubCount(pid);
        }
        //新父节点子部门数量处理
        if (dept.getPid() != null && flag) {
            this.dealParentDeptSubCount(dept.getPid());
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Long> ids) {
        if (ids.size() > 0) {
            List<Dept> deptList = this.baseMapper.selectList(new LambdaQueryWrapper<Dept>().in(Dept::getId, ids));
            //删除叶子节点时同步更新父节点的子部门数量
            Set<Long> pidSet = deptList.stream().map(Dept::getPid).filter(Objects::nonNull).collect(Collectors.toSet());
            //删除父节点时同步删除子节点
            List<Long> childrenIds = this.baseMapper.selectList(new LambdaQueryWrapper<Dept>().in(Dept::getPid, ids)).stream().map(Dept::getId).collect(Collectors.toList());
            boolean flag = this.removeByIds(ids);
            if (flag) {
                if (pidSet.size() > 0) {
                    for (Long pid : pidSet) {
                        if (!ids.contains(pid)) {
                            Dept parentDept = this.getById(pid);
                            int subCount = this.baseMapper.selectList(new LambdaQueryWrapper<Dept>().eq(Dept::getPid, pid)).size();
                            parentDept.setSubCount(subCount);
                            this.updateById(parentDept);
                        }
                    }
                }
                if (childrenIds.size() > 0) {
                    this.removeByIds(childrenIds);
                }
            }
            return flag;
        }
        return false;
    }

    @Override
    public Dept queryById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("部门id不能为空");
        }
        return this.getById(id);
    }

    @Override
    public Dept queryByName(String name) {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.lambda().eq(Dept::getName, name);
        }
        return this.getOne(queryWrapper);
    }

    @Override
    public List<Dept> queryList() {
        return this.list();
    }

    @Override
    public Page<DeptDto> queryPage(Dept dept, QueryRequest queryRequest) {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dept.getName())) {
            queryWrapper.lambda().like(Dept::getName, dept.getName());
        }
        if (dept.getEnabled() != null) {
            queryWrapper.lambda().eq(Dept::getEnabled, dept.getEnabled());
        }
        if (StringUtils.isBlank(dept.getName()) && dept.getEnabled() == null) {
            //默认查询一级部门
            queryWrapper.lambda().isNull(Dept::getPid);
        }
        Page<Dept> page = this.page(new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize()), queryWrapper);
        List<DeptDto> deptDtoList = page.getRecords().stream().map(item -> {
            DeptDto deptDto = new DeptDto();
            BeanUtils.copyProperties(item, deptDto);
            return deptDto;
        }).collect(Collectors.toList());
        return new Page<DeptDto>().setRecords(deptDtoList).setSize(deptDtoList.size());
    }

    @Override
    public List<DeptDto> queryChildListByPid(Long pid) {
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dept::getEnabled, true);
        if (pid == null) {
            queryWrapper.isNull(Dept::getPid);
        } else {
            queryWrapper.eq(Dept::getPid, pid);
        }
        List<Dept> deptList = this.baseMapper.selectList(queryWrapper);
        if (deptList != null && deptList.size() > 0) {
            return deptList.stream().map(dept -> {
                DeptDto deptDto = new DeptDto();
                BeanUtils.copyProperties(dept, deptDto);
                return deptDto;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<? extends Tree<?>> queryDeptTree() {
        List<Dept> deptList = this.baseMapper.selectList(new LambdaQueryWrapper<Dept>().eq(Dept::getEnabled, true));
        if (deptList != null && deptList.size() > 0) {
            List<DeptDtoTree> trees = new ArrayList<>();
            deptList.forEach(dept -> {
                DeptDtoTree tree = new DeptDtoTree();
                tree.setId(dept.getId());
                tree.setParentId(dept.getPid() == null ? null : dept.getPid());
                tree.setLabel(dept.getName());
                tree.setSubCount(dept.getSubCount());
                tree.setName(dept.getName());
                tree.setSort(dept.getSort());
                trees.add(tree);
            });
            return TreeUtil.build(trees);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Dept> querySuperiorList(Dept dept, List<Dept> deptList) {
        if (dept.getPid() == null) {
            deptList.addAll(this.baseMapper.selectList(new LambdaQueryWrapper<Dept>().isNull(Dept::getPid)));
            return deptList;
        }
        deptList.addAll(this.baseMapper.selectList(new LambdaQueryWrapper<Dept>().eq(Dept::getPid, dept.getPid())));
        return querySuperiorList(this.getById(dept.getPid()), deptList);
    }

    @Override
    public List<Dept> queryDeptChildren(List<Dept> deptList) {
        List<Dept> result = new ArrayList<>();
        if (deptList != null && deptList.size() > 0) {
            for (Dept dept : deptList) {
                List<Dept> depts = this.baseMapper.selectList(new LambdaQueryWrapper<Dept>().eq(Dept::getPid, dept.getId()));
                if (depts != null && depts.size() > 0) {
                    result.addAll(queryDeptChildren(depts));
                }
                result.add(dept);
            }
        }
        return result;
    }

    /**
     * 父节点子部门数量处理
     *
     * @param pid 父ID
     */
    private void dealParentDeptSubCount(Long pid) {
        Dept parentDept = this.queryById(pid);
        int subCount = this.baseMapper.selectList(new LambdaQueryWrapper<Dept>().eq(Dept::getPid, pid)).size();
        if (parentDept.getSubCount() != subCount) {
            parentDept.setSubCount(subCount);
            this.updateById(parentDept);
        }
    }

}
