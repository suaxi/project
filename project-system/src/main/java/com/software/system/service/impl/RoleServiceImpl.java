package com.software.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.dto.QueryRequest;
import com.software.exception.BadRequestException;
import com.software.system.entity.Dept;
import com.software.system.entity.Role;
import com.software.system.entity.RoleDept;
import com.software.system.entity.RoleMenu;
import com.software.system.mapper.RoleMapper;
import com.software.system.service.RoleDeptService;
import com.software.system.service.RoleMenuService;
import com.software.system.service.RoleService;
import com.software.utils.SecurityUtils;
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

    @Autowired
    private RoleDeptService roleDeptService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(Role role) {
        role.setCreateBy(SecurityUtils.getCurrentUserId());
        boolean flag = this.save(role);
        if (flag) {
            //当角色数据范围是“自定义”时，同步保存 角色-部门 关联数据
            List<Dept> deptList = role.getDepts();
            if (deptList != null && deptList.size() >0) {
                List<RoleDept> roleDeptList = new ArrayList<>();
                for (Dept dept : deptList) {
                    RoleDept roleDept = new RoleDept();
                    roleDept.setRoleId(role.getId());
                    roleDept.setDeptId(dept.getId());
                    roleDeptList.add(roleDept);
                }
                return roleDeptService.saveBatch(roleDeptList);
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Role role) {
        //角色名称校验
        Role roleById = this.getById(role.getId());
        Role roleByName = this.queryByName(role.getName());
        if (roleByName != null && !roleByName.getId().equals(roleById.getId())) {
            throw new BadRequestException("该角色名称已存在，请重新输入");
        }
        role.setUpdateBy(SecurityUtils.getCurrentUserId());
        boolean flag = this.updateById(role);
        if (flag) {
            //当角色数据范围是“自定义”时，同步保存 角色-部门 关联数据
            List<Dept> deptList = role.getDepts();
            if (deptList != null && deptList.size() >0) {
                roleDeptService.deleteRoleDeptByRoleId(Collections.singletonList(role.getId()));
                List<RoleDept> roleDeptList = new ArrayList<>();
                for (Dept dept : deptList) {
                    RoleDept roleDept = new RoleDept();
                    roleDept.setRoleId(role.getId());
                    roleDept.setDeptId(dept.getId());
                    roleDeptList.add(roleDept);
                }
                return roleDeptService.saveBatch(roleDeptList);
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Long> ids) {
        boolean flag = this.removeByIds(ids);
        if (flag) {
            //删除角色-菜单，角色-部门关联数据
            roleMenuService.deleteRoleMenuByRoleId(ids);
            roleDeptService.deleteRoleDeptByRoleId(ids);
        }
        return flag;
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
    public void updateRoleMenu(Long roleId, List<Long> menuIds) {
        //删除关联数据
        roleMenuService.deleteRoleMenuByRoleId(Collections.singletonList(roleId));

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
