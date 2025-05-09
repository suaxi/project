package com.software.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.dto.QueryRequest;
import com.software.system.entity.Role;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:36
 */
public interface RoleService {

    /**
     * 新增
     *
     * @param role 角色信息
     * @return
     */
    boolean add(Role role);

    /**
     * 修改
     *
     * @param role 角色信息
     * @return
     */
    boolean update(Role role);

    /**
     * 删除
     *
     * @param ids ids
     * @return
     */
    boolean delete(List<Integer> ids);

    /**
     * 根据id查询角色信息
     *
     * @param id id
     * @return
     */
    Role queryById(Integer id);

    /**
     * 根据名称查询角色信息
     *
     * @param name 角色名称
     * @return
     */
    Role queryByName(String name);

    /**
     * 查询角色列表
     *
     * @return
     */
    List<Role> queryList(Role role);

    /**
     * 分页查询
     *
     * @param role         角色信息
     * @param queryRequest 查询参数
     * @return
     */
    Page<Role> queryPage(Role role, QueryRequest queryRequest);

    /**
     * 根据用户id查询数据权限
     *
     * @param userId
     * @return
     */
    List<String> queryDataScopeByUserId(Integer userId);

    /**
     * 根据用户id查询角色列表
     *
     * @param userId 用户id
     * @return 角色列表
     */
    List<Role> queryRoleListByUserId(Integer userId);

    /**
     * 根据角色id修改角色关联菜单
     *
     * @param roleId  角色id
     * @param menuIds 菜单ids
     */
    void updateRoleMenu(Integer roleId, List<Integer> menuIds);
}

