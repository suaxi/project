package com.software.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.software.system.entity.RoleMenu;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/11/5 16:52
 */
public interface RoleMenuService extends IService<RoleMenu> {

    /**
     * 根据角色id删除角色菜单关联数据
     * @param roleIds 角色ids
     */
    void deleteRoleMenuByRoleId(List<Long> roleIds);
}
