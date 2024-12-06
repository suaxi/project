package com.software.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.software.system.entity.UserRole;

/**
 * @author Wang Hao
 * @date 2022/10/25 21:47
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 根据用户id删除用户角色关联数据
     * @param userIds 用户ids
     */
    void deleteUserRoleByUserId(Integer[] userIds);
}
