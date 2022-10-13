package com.software.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.entity.Role;
import com.software.mapper.RoleMapper;
import com.software.service.RoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(Role role) {
        //TODO 创建人信息
        return this.save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Role role) {
        return this.updateById(role);
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
    public Role queryById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("角色id不能为空");
        }
        return this.getById(id);
    }

    @Override
    public Role queryByName(String name) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.lambda().eq(Role::getName, name);
        }
        return this.getOne(queryWrapper);
    }

    @Override
    public List<Role> queryList() {
        return this.list();
    }

    @Override
    public Page<Role> queryPage(Role role, QueryRequest queryRequest) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(role.getName())) {
            queryWrapper.lambda().eq(Role::getName, role.getName());
        }
        if (role.getLevel() != null) {
            queryWrapper.lambda().eq(Role::getLevel, role.getLevel());
        }
        if (StringUtils.isNotBlank(role.getDataScope())) {
            queryWrapper.lambda().eq(Role::getDataScope, role.getDataScope());
        }
        if (StringUtils.isNotBlank(queryRequest.getOrder())) {
            queryWrapper.orderBy(true, StringConstant.ASC.equals(queryRequest.getOrder()), queryRequest.getField());
        } else {
            queryWrapper.orderBy(true, false, "create_time");
        }
        Page<Role> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.page(page, queryWrapper);
    }
}
