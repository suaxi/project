package com.software.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.system.entity.Dept;
import com.software.system.mapper.DeptMapper;
import com.software.system.service.DeptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:38
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(Dept dept) {
        //TODO 创建人信息
        return this.save(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Dept dept) {
        return this.updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long[] ids) {
        if (ids.length > 0) {
            return this.removeByIds(Arrays.asList(ids));
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
    public Page<Dept> queryPage(Dept dept, QueryRequest queryRequest) {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dept.getName())) {
            queryWrapper.lambda().eq(Dept::getName, dept.getName());
        }
        if (dept.getEnabled() != null) {
            queryWrapper.lambda().eq(Dept::getEnabled, dept.getEnabled());
        }
        if (dept.getSort() != null) {
            queryWrapper.lambda().eq(Dept::getSort, dept.getSort());
        }
        if (StringUtils.isNotBlank(queryRequest.getOrder())) {
            queryWrapper.orderBy(true, StringConstant.ASC.equals(queryRequest.getOrder()), queryRequest.getField());
        } else {
            queryWrapper.orderBy(true, false, "create_time");
        }
        Page<Dept> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Dept> queryChildListByPid(Long pid) {
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        if (pid == null) {
            queryWrapper.isNull(Dept::getPid);
        } else {
            queryWrapper.eq(Dept::getPid, pid);
        }
        return this.baseMapper.selectList(queryWrapper);
    }
}
