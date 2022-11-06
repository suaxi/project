package com.software.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.software.system.entity.RoleDept;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/11/5 22:51
 */
public interface RoleDeptService extends IService<RoleDept> {

    /**
     * 根据角色id删除角色部门关联数据
     * @param roleIds 角色ids
     */
    void deleteRoleDeptByRoleId(List<Long> roleIds);
}
