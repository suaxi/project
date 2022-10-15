package com.software.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.dto.QueryRequest;
import com.software.system.entity.Menu;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:36
 */
public interface MenuService {

    /**
     * 新增
     *
     * @param menu 菜单信息
     * @return
     */
    boolean add(Menu menu);

    /**
     * 修改
     *
     * @param menu 菜单信息
     * @return
     */
    boolean update(Menu menu);

    /**
     * 删除
     *
     * @param ids ids
     * @return
     */
    boolean delete(Long[] ids);

    /**
     * 根据id查询菜单信息
     *
     * @param id id
     * @return
     */
    Menu queryById(Long id);

    /**
     * 根据名称查询菜单信息
     *
     * @param name 菜单名称
     * @return
     */
    Menu queryByName(String name);

    /**
     * 查询菜单列表
     *
     * @return
     */
    List<Menu> queryList();

    /**
     * 分页查询
     *
     * @param menu         菜单信息
     * @param queryRequest 查询参数
     * @return
     */
    Page<Menu> queryPage(Menu menu, QueryRequest queryRequest);

    /**
     * 根据用户id查询用户菜单权限
     *
     * @param userId 用户id
     * @return
     */
    List<String> queryUserPermissionByUserId(Long userId);
}
