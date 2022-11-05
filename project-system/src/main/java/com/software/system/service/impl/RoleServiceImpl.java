package com.software.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.dto.QueryRequest;
import com.software.exception.BadRequestException;
import com.software.system.entity.Role;
import com.software.system.entity.RoleMenu;
import com.software.system.mapper.RoleMapper;
import com.software.system.service.RoleMenuService;
import com.software.system.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:38
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuService roleMenuService;

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
        Page<Role> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.baseMapper.queryPage(page, role);
    }

    @Override
    public List<String> queryDataScopeByUserId(Long userId) {
        if (userId != null) {
            return roleMapper.queryDataScopeByUserId(userId);
        }
        return null;
    }

    @Override
    public List<Role> queryRoleListByUserId(Long userId) {
        if (userId == null) {
            throw new BadRequestException("用户id不能为空");
        }
        return roleMapper.queryRoleListByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleMenu(Long roleId, Long[] menuIds) {
        //删除关联数据
        Long[] roleIds = Collections.singletonList(roleId).toArray(new Long[0]);
        roleMenuService.deleteRoleMenuByRoleId(roleIds);

        List<RoleMenu> roleMenuList = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuList.add(roleMenu);
        }
        roleMenuService.saveBatch(roleMenuList);
    }
}
