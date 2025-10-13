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
        dept.setCreateUser(SecurityUtils.getCurrentUsername());
        boolean flag = this.save(dept);
        if (flag) {
            if (dept.getPid() == 0) {
                return true;
            }
            Dept parentDept = this.getById(dept.getPid());
            if (parentDept != null) {
                parentDept.setSubCount(parentDept.getSubCount() + 1);
                this.updateById(parentDept);
            }
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Dept dept) {
        Dept oldDeptData = this.getById(dept.getId());
        dept.setUpdateUser(SecurityUtils.getCurrentUsername());
        boolean flag = this.updateById(dept);
        if (flag) {
            //原父节点子部门数量处理
            this.dealParentDeptSubCount(oldDeptData.getPid() == 0 ? oldDeptData.getId() : oldDeptData.getPid());
            //新父节点子部门数量处理
            this.dealParentDeptSubCount(dept.getPid() == 0 ? dept.getId() : dept.getPid());
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Integer> ids) {
        if (ids.size() > 0) {
            List<Dept> deptList = this.baseMapper.selectList(new LambdaQueryWrapper<Dept>().in(Dept::getId, ids));
            //删除叶子节点时同步更新父节点的子部门数量
            Set<Integer> pidSet = deptList.stream().map(Dept::getPid).filter(Objects::nonNull).collect(Collectors.toSet());
            //删除父节点时同步删除子节点
            List<Integer> childrenIds = this.baseMapper.selectList(new LambdaQueryWrapper<Dept>().in(Dept::getPid, ids)).stream().map(Dept::getId).collect(Collectors.toList());
            boolean flag = this.removeByIds(ids);
            if (flag) {
                if (pidSet.size() > 0) {
                    for (Integer pid : pidSet) {
                        if (!ids.contains(pid)) {
                            Dept parentDept = this.getById(pid);
                            if (parentDept != null) {
                                int subCount = this.baseMapper.selectList(new LambdaQueryWrapper<Dept>().eq(Dept::getPid, pid)).size();
                                parentDept.setSubCount(subCount);
                                this.updateById(parentDept);
                            }
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
    public Dept queryById(Integer id) {
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
    public List<DeptDto> queryChildListByPid(Integer pid) {
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dept::getEnabled, true);
        queryWrapper.eq(Dept::getPid, pid);
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
    public List<? extends Tree<?>> queryDeptTree(Dept dept) {
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(dept.getName())) {
            queryWrapper.like(Dept::getName, dept.getName());
        }
        if (dept.getEnabled() != null) {
            queryWrapper.eq(Dept::getEnabled, dept.getEnabled());
        }
        List<Dept> deptList = this.list(queryWrapper);
        if (deptList != null && deptList.size() > 0) {
            Set<Dept> deptSet = new HashSet<>();
            this.queryParentDeptList(deptList, deptSet);
            if (deptSet.size() > 0) {
                List<DeptDtoTree> trees = new ArrayList<>();
                for (Dept item : deptSet) {
                    DeptDtoTree tree = new DeptDtoTree();
                    tree.setId(item.getId());
                    tree.setParentId(item.getPid());
                    tree.setLabel(item.getName());
                    tree.setSubCount(item.getSubCount());
                    tree.setName(item.getName());
                    tree.setEnabled(item.getEnabled());
                    tree.setSort(item.getSort());
                    trees.add(tree);
                }
                return TreeUtil.build(trees);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<Dept> querySuperiorList(Dept dept, List<Dept> deptList) {
        if (dept.getPid() == 0) {
            deptList.addAll(this.baseMapper.selectList(new LambdaQueryWrapper<Dept>().eq(Dept::getPid, 0)));
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
     * 根据id向上递归查找父级部门
     *
     * @param deptList 部门列表
     * @param deptSet  查询结果集
     * @return 父级部门列表
     */
    private Set<Dept> queryParentDeptList(List<Dept> deptList, Set<Dept> deptSet) {
        for (Dept dept : deptList) {
            deptSet.add(dept);
            if (dept.getPid() == 0) {
                continue;
            }

            List<Dept> depts = this.baseMapper.selectList(new LambdaQueryWrapper<Dept>().eq(Dept::getId, dept.getPid()));
            if (depts != null && depts.size() > 0) {
                queryParentDeptList(depts, deptSet);
            }
        }
        return deptSet;
    }

    /**
     * 父节点子部门数量处理
     *
     * @param pid 父ID
     */
    private void dealParentDeptSubCount(Integer pid) {
        Dept parentDept = this.queryById(pid);
        int subCount = this.baseMapper.selectList(new LambdaQueryWrapper<Dept>().eq(Dept::getPid, parentDept.getId())).size();
        if (parentDept.getSubCount() != subCount) {
            parentDept.setSubCount(subCount);
            this.updateById(parentDept);
        }
    }

}
